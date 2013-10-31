<?php

class SewController extends Controller
{

	const RENUMERATION_POWER = 10;
	const BONUS_POWER = 10;
	const NEWLINE_CHARACTER = "<br />";

	// New SEW
	const SENSOR_QUALITY = 1.0;
	const SENSOR_ELASTICITY = 1.0;
	const CONTRIBUTOR_ELASTICITY = 1.0;
	const BATCH_REWARD = 10;

	/**
	 * @var string the default layout for the views. Defaults to '//layouts/column2', meaning
	 * using two-column layout. See 'protected/views/layouts/column2.php'.
	 */
	public $layout='//layouts/column1';

	/**
	 * @return array action filters
	 */
	public function filters()
	{
		return array(
			'accessControl', // perform access control for CRUD operations
		);
	}

	/**
	 * Specifies the access control rules.
	 * This method is used by the 'accessControl' filter.
	 * @return array access control rules
	 */
	public function accessRules()
	{
		return array(
			array('allow',  // allow all users to perform 'index' and 'view' actions
				'actions'=>array('calcSew', 'calcIPLP', 'index', 'calcNewSew'),
				'users'=>array('*'),
			),
			array('deny',  // deny all users
				'users'=>array('*'),
			),
		);
	}

	public function actionIndex()
	{
		echo 'Hello world 2!';
		Yii::app()->end();
	}

	// Helper function: distance
	// Calculates the distance between two latitude/longitude points, in metres
	// distance between two lat/long in metres
	private function distance($lat1, $lon1, $lat2, $lon2)
	{
	    $radlat1 = pi() * $lat1 / 180;
	    $radlat2 = pi() * $lat2 / 180;
	    $radlon1 = pi() * $lon1 / 180;
	    $radlon2 = pi() * $lon2 / 180;
	    $radtheta = pi() * ($lon1 - $lon2) / 180;
	    return round(6371 * 1000 * acos(sin($radlat1) * sin($radlat2) + cos($radlat1) * cos($radlat2) * cos($radtheta)));
	}

	public function actionCalcIPLP()
	{
		$allUsers = User::model()->findAll();

		foreach ($allUsers as $user)
		{
			// IP update
			$neout = count($user->endorseeEntries);
			if ($neout == 0) {
				$user->ip = 0;
			} else {
				$user->ip = floatval($user->cp + $user->ep) / $neout;
			}
		}

		foreach ($allUsers as $user)
		{
			// LP update
			// Select all users whose endorseeEntries contain an endorsement to the contributor
			$endorsers = User::model()->with(array(
				'endorseeEntries' => array(
					'select' => false,
					'joinType' => 'INNER JOIN',
					'condition' => 'endorseeEntries.endorsee = ' . $user->uid,
				),
			))->findAll();

			$user->lp = 0;
			foreach ($endorsers as $endorser)
			{
				$user->lp += $endorser->ip;
			}
		}

		$transaction = Yii::app()->db->beginTransaction();
		try {
			foreach ($allUsers as $user)
			{
				$user->save();
			}
			$transaction->commit();
		} catch (Exception $e) {
			$transaction->rollback();
		}
	}
	
	public function actionCalcSew()
	{
		/*
		$users = User::model()->findAll();
		$output = array();
		$i = 0;

		foreach ($users as $user)
		{
			$output[$i] = array();
			$output[$i]['user'] = $user->attributes;
			$posts=Post::model()->findAll('uid=:uid', array(':uid' => $user->uid));

			$output[$i]['posts'] = $posts;

			$i++;
		}
		*/

		// Begin SEW calculations

		/* ----------------------------------
			This is a port of the old SEW
		---------------------------------- */

		// --------------
		// Clustering
		// --------------

		// Find all unprocessed posts without cluster id
		$unlabelledPosts = Post::model()->findAllByAttributes(array('clusterid' => 0,
																	'verdictid' => 0));
		if (empty($unlabelledPosts)) {
			echo 'everything is clustered!';
			return;
		}

		// Find all unprocessed posts with cluster id
		$labelledPosts = Post::model()->findAllByAttributes(
											array('verdictid' => 0),
											'clusterid!=:cid',
											array(':cid' => 0)
											);
		if (empty($labelledPosts)) {
			echo 'all clustered posts have verdicts!';
			return;
		}

		// Get largest cluster ID
		$maxClusterCriteria = new CDbCriteria;
		$maxClusterCriteria->select = 'MAX(clusterid) as maxCluster';
		$maxClusterPost = Post::model()->find($maxClusterCriteria);
		$maxClusterID = $maxClusterPost->maxCluster;

		// For each post without cluster ID, we try to determine one.
		// Set a time interval for posts to be related.
		$timeInterval = 3 * 24 * 60 * 60; // 3 * 1 day = 24 hours = ...

		while ($post = array_pop($unlabelledPosts))
		{
			echo 'start iteration on unlabelled post with id = ' . $post->id;
			echo SewController::NEWLINE_CHARACTER;

			// Find time in seconds
			$postTime = strtotime($post->created_on);
			$postStartTime = $timeInterval * floor($postTime / $timeInterval);
			$postEndTime = $postStartTime + $timeInterval;

			// Attempt to find a post in the same spacetime
			foreach ($labelledPosts as $possibleNearbyPost)
			{
				echo 'checking whether labelled post with id = ' . $possibleNearbyPost->id . ' is near';
				echo SewController::NEWLINE_CHARACTER;

				// Check whether spacetime intersect
				$possibleNPTime = strtotime($possibleNearbyPost->created_on);
				$spaceDifference = $this->distance(floatval($post->lat), floatval($post->lng), floatval($possibleNearbyPost->lat), floatval($possibleNearbyPost->lng));
				$possibleNPInTimeInterval = (($possibleNPTime >= $postStartTime)
												&& ($possibleNPTime < $postEndTime));
				if (($spaceDifference < 10) && ($possibleNPInTimeInterval))
				{
					echo 'YES NEARBY!';
					echo SewController::NEWLINE_CHARACTER;

					// Assign this post to the cluster
					$post->clusterid = $possibleNearbyPost->clusterid;
					// $post->save();

					// Check if there are other nearby posts that should be clustered with this post
					foreach ($labelledPosts as $possibleNearbyClusterPost)
					{
						echo 'checking whether there is a nearby cluster to be joined with this: check post with id = ' . $possibleNearbyClusterPost->id;
						echo SewController::NEWLINE_CHARACTER;

						$possibleNCPTime = strtotime($possibleNearbyClusterPost->created_on);
						$spaceDifference = $this->distance(floatval($post->lat), floatval($post->lng), floatval($possibleNearbyClusterPost->lat), floatval($possibleNearbyClusterPost->lng));
						$possibleNCPTimeInterval = (($possibleNCPTime >= $postStartTime)
													&& ($possibleNCPTime < $postEndTime));
						if (($spaceDifference < 10) && ($possibleNCPTimeInterval)
							&& ($post->clusterid != $possibleNearbyClusterPost->clusterid))
						{
							echo 'found a nearby cluster to mesh the cluster together';
							echo SewController::NEWLINE_CHARACTER;

							// Find all posts with the old cluster ID and change it to the new cluster ID
							// This updates old clusters to a new spacetime
							$oldCID = $possibleNearbyClusterPost->clusterid;
							$newCID = $post->clusterid;
							foreach ($labelledPosts as $possibleUpdateClusterPost)
							{
								if ($possibleUpdateClusterPost->clusterid == $oldCID)
									$possibleUpdateClusterPost->clusterid = $newCID;
									// $possibleUpdateClusterPost->save();
							}
						}
					}

					// We have found a cluster for for this particular post; no need to loop anymore
					break;
				}
			}

			// If we can't find any clusters to insert the point, create a new cluster.
			if ($post->clusterid == 0)
			{
				echo 'not nearby - add a cluster';
				echo SewController::NEWLINE_CHARACTER;
				$post->clusterid = ++$maxClusterID;
				// $post->save();
			}

			// Add this new cluster to the list of clusters
			array_push($labelledPosts, $post);

			// Look for next unlabelled post
		}

		$transaction = Yii::app()->db->beginTransaction();
		try {
			foreach ($labelledPosts as $lpost)
			{
				$lpost->save();
			}
			$transaction->commit();
		} catch (Exception $e) {
			$transaction->rollback();
		}

		// -------------------
		// SEW Calculations
		// -------------------

		// First get the list of non-verdicted posts
		$noVerdictCriteria = new CDbCriteria;
		// $noVerdictCriteria->select = 'MAX(id) as id';
		// $noVerdictCriteria->condition = "clusterid != 0 AND verdictid = 0";
		// $noVerdictCriteria->group = "clusterid, uid";
		$noVerdictCriteria->condition = "id IN (SELECT MAX(id) id FROM post WHERE clusterid!='0' AND verdictid='0' GROUP BY clusterid, uid)";
		$noVerdictPosts = Post::model()->findAll($noVerdictCriteria);
		if (empty($noVerdictPosts)) {
			echo 'Every post has a verdict! Returning..';
			return;
		}

		// Sort them into their individual clusters
		$verdictClusters = array();
		foreach ($noVerdictPosts as $nvp)
		{
			// We only consider posts before the current time interval
			$startTime = $timeInterval * floor(time() / $timeInterval);
			if ((strtotime($nvp->created_on) < $startTime)) {
				$vID = $nvp->clusterid;
				if (!isset($verdictClusters[$vID])) $verdictClusters[$vID] = array();
				array_push($verdictClusters[$vID], $nvp);
			} else {
				$nvp->verdictid = -1;
				$nvp->save();
			}
		}

		// Calculate verdict as per SEW
		$verdicts = array();
		foreach ($verdictClusters as $vID => $vCluster)
		{

			// ---------------------
			// (B) Consensus Score
			// ---------------------

			$totalCPLP = 0;

			foreach ($vCluster as &$vObservation)
			{
				// Update total to divide later
				$totalCPLP += ($vObservation->creator->cp + $vObservation->creator->lp);

				$vObservation->wz = $vObservation->creator->cp + $vObservation->creator->lp;
			}

			// Divide and calculate zstar (consensus)
			$zstar = 0;
			foreach ($vCluster as &$vObservation)
			{
				$vObservation->wz /= $totalCPLP;
				$zstar += $vObservation->wz * $vObservation->severity;
			}

			// create new verdict
			$verdict = new Verdict;
			$verdict->trust = $zstar;

			array_push($verdicts, $verdict);

			// ---------------------
			// (C) Rewards
			// ---------------------

			// Residual
			// calculate max(|z - z*|)
			$ec_bottom = 0;
			foreach($vCluster as $observation)
			{
				$deviation = abs($observation->wz - $zstar); // -------------- THIS PLACE IS DIFFERENT FROM MARCUS
				if ($deviation > $ec_bottom) $ec_bottom = $deviation;
			}

			foreach($vCluster as $observation)
			{
				// calculate |zc - z*|
				$ec = 0;
				$deviation = abs($observation->wz - $zstar);
				if (!(count($vCluster) == 1)) $ec = floatval($deviation) / $ec_bottom;

				// Increase CP of contributor
				$contributor = $observation->creator;
				$contributor->cp += (1 - $ec) * SewController::RENUMERATION_POWER;
				$contributor->save();

				// Profit sharing - find all endorsers to increase their EP
				// Select all users whose endorseeEntries contain an endorsement to the contributor
				$endorsers = User::model()->with(array(
					'endorseeEntries' => array(
						'select' => false,
						'joinType' => 'INNER JOIN',
						'condition' => 'endorseeEntries.endorsee = ' . $contributor->uid,
					),
				))->findAll();

				foreach ($endorsers as $endorser)
				{
					// profit sharing ratio
					$nec = 0;
					if ($contributor->lp != 0)
						$nec = floatval($endorser->ip) / $contributor->lp;

					$endorser->ep += $nec * (1 - $ec) * SewController::BONUS_POWER;
					$endorser->save();
				}
			}

			// Update processed posts so they don't get chosen again
			foreach ($vCluster as &$vObservation)
			{
				$vObservation->verdictid = $vObservation->clusterid;
				$vObservation->save();
			}

		}
		
		$transaction = Yii::app()->db->beginTransaction();
		try {
			foreach ($verdicts as $verdict)
			{
				$verdict->save();
			}
			$transaction->commit();
		} catch (Exception $e) {
			$transaction->rollback();
		}
		
		/*
		1)
		Retrieve user's CP

		2) Calculate user LP
			a) Get all endorsers of user
			b) For each endorser, calculate IP
				i) Add up endorser's CP + EP
				ii) Get SUM(people endorsed by endorser)
				iii) (i)/(ii)
			c) Sum up the IP = LP

		*/

		// End SEW calculations

		$output = array();
		$jsonObj=CJSON::encode($output);
		echo $jsonObj;
		Yii::app()->end();

	}

	// New SEW.
	public function actionCalcNewSew()
	{
		// -------------------------------------
		// Step 1: Calculate BP of every user
		// -------------------------------------
		$allUsers = User::model()->findAll();

		foreach ($allUsers as $user)
		{
			// Select all users whose endorseeEntries contain an endorsement to the contributor
			$endorsers = User::model()->with(array(
				'endorseeEntries' => array(
					'select' => false,
					'joinType' => 'INNER JOIN',
					'condition' => 'endorseeEntries.endorsee = ' . $user->uid,
				),
			))->findAll();

			$user->bp = 0;

			foreach ($endorsers as $endorser)
			{
				$user->bp += ($endorser->ep) / count($endorser->endorseeEntries);
			}

			// Initialize p_k for Step 3 calculations
			$user->p_k = 0;
		}

		// Save BP
		$transaction = Yii::app()->db->beginTransaction();
		try {
			foreach ($allUsers as $user)
			{
				$user->save();
			}
			$transaction->commit();
		} catch (Exception $e) {
			$transaction->rollback();
		}

		// ---------------------------
		// Step 2: Calculate r_k
		// ---------------------------
		// We make some basic assumptions on the constant values.
		// $q_k_alpha = pow(SewController::SENSOR_QUALITY, SewController::SENSOR_ELASTICITY);

		$cri = new CDbCriteria;
		$cri->addCondition('modified_on > ' . new CDbExpression("DATE_SUB(NOW(), INTERVAL 4 HOUR)"));
		$todayposts = Post::model()->with('creator')->findAll();

		$postedUsers = array();
		foreach ($todayposts as $tpost)
		{
			if (!isset($postedUsers[$tpost->uid])) {
				$postedUsers[$tpost->uid] = $tpost->creator;
				$postedUsers[$tpost->uid]->q_k++;
			}
		}

		// Denominator
		$total_contribution = 0;
		foreach ($postedUsers as $user)
		{
			// PTI --> power_transfer_impact
			$user->pti = ($user->bp) / ($user->bp + $user->cp);
			$total_contribution += pow($user->q_k, SewController::SENSOR_ELASTICITY) * pow(($user->cp * (1 + $user->pti)), SewController::CONTRIBUTOR_ELASTICITY);
		}

		// We use LP as a temporary storage for r_k
		foreach ($postedUsers as $user)
		{
			$numerator = pow($user->q_k, SewController::SENSOR_ELASTICITY) * pow(($user->cp * (1 + $user->pti)), SewController::CONTRIBUTOR_ELASTICITY);
			$user->r_k = ($numerator / $total_contribution) * SewController::BATCH_REWARD;

			// Increase contribution power
			$user->cp += $user->r_k;
		}

		// ----------------------------
		// Step 3: Calculate p_k
		// ----------------------------
		// Only have p_k for contributed users
		foreach ($postedUsers as $user)
		{
			$user->p_k = $user->pti / (1 + $user->pti);
		}

		// Save pti, r_k and p_k
		$transaction = Yii::app()->db->beginTransaction();
		try {
			foreach ($postedUsers as $user)
			{
				$user->save();
			}
			$transaction->commit();
		} catch (Exception $e) {
			$transaction->rollback();
		}

		// Reload
		$allUsers = User::model()->findAll();

		// -------------------------------------
		// Step 4: Calculate increase in EP
		// -------------------------------------
		foreach ($allUsers as $user)
		{
			// Select all users whose endorserEntries contain an endorsement by the user
			$endorsees = User::model()->with(array(
				'endorserEntries' => array(
					'select' => false,
					'joinType' => 'INNER JOIN',
					'condition' => 'endorserEntries.endorser = ' . $user->uid,
				),
			))->findAll();

			$ep_increase = 0;

			// Note possible division by zero case: $endorsee->bp is 0
			// This means that the endorsee do not have any endorsers 
			
			foreach ($endorsees as $endorsee)
			{
				$nik = $user->ep / (count($endorsees) * $endorsee->bp);
				$ep_increase += $nik * $endorsee->p_k * $endorsee->r_k;
			}
			
			/*
			foreach ($allUsers as $possibleEndorsee) {
				foreach ($endorsees as $endorsee) {
					// Attempt to check whether each user is an endorsee of the current user in question
					if ($possibleEndorsee->uid == $endorsee->uid) {
						// We have found an endorsee
						$nik = $user->ep / (count($endorsees) * $possibleEndorsee->bp);
						$ep_increase += $nik * 
					}
				}
			}
			*/

			$user->ep += $ep_increase;
		}

		$transaction = Yii::app()->db->beginTransaction();
		try {
			foreach ($allUsers as $user)
			{
				$user->save();
			}
			$transaction->commit();
		} catch (Exception $e) {
			$transaction->rollback();
		}
	}
	

}
?>
