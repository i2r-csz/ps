<?php

class MpostController extends Controller
{
	const LIMIT_PER_PAGE=100;

	const END_REQUESTING = 0;
	const END_ACCEPTED = 1;
	const END_REJECTED = 2;

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
				'actions'=>array('list','get','upload', 'login', 'users', 'getUser', 'endorse', 'register', 'thumbs'),
				'users'=>array('*'),
			),
			array('deny',  // deny all users
				'users'=>array('*'),
			),
		);
	}

	public function actionRegister(){
		if(!empty($_POST['User'])){
			$user=new User('mobile');
			$user->attributes=$_POST['User'];
			if($user->validate()){
				$user->save();

				$rUser = array();
				$rUser = $user->attributes;
				$rUser['endorseemnts'] = array();
				$rUser['endorseReceives'] = array();

				$jasonObj=CJSON::encode($rUser);
				echo $jasonObj;
				Yii::app()->end();		
				
			}else{
				print_r($user->errors);
			}	
		}else{
			echo 'User empty';
		}
	}

	// Gets the list of users (for leaderboard purposes)
	// POST variables:
	// SORT_BY		string 		CP/EP/IP/LP
	public function actionUsers(){
		if(isset($_POST['SORT_BY'])){
			$sortBy=$_POST['SORT_BY'];
			$users=User::model()->findAll();

			$arr=array();
			$i=0;
			foreach($users as $user)
			{
				$arr[$i]=$user->attributes;
				$filename='';
				if(!empty($user->avatar)){
					$filename=$user->avatar->filename;
				}
				$arr[$i]['filename']=$filename;
				$i++;
			}
			
			
		
			$jasonObj=CJSON::encode($arr);
			echo $jasonObj;
			Yii::app()->end();		
		}
	}

	// Logs in to the system
	// POST variables:
	// User['email'] 		string 		username
	// User['password']		string 		password
	public function actionLogin()
	{
		$ret=-1;

		if(isset($_POST['User'])){
			$user=new User;
			$user->setAttributes($_POST['User']);
			$user->password=$_POST['User']['password'];

			$condition='email=:email AND password=:password';
			$params=array('email'=>$user->email, 'password'=>md5($user->password));
			$dbUser=User::model()->find($condition, $params);

			if(!empty($dbUser)) {
				// Found the user. Proceed to return endorsements
				$rUser = array();
				$rUser = $dbUser->attributes;
				$rUser['endorsements'] = Endorsement::model()->findAllByAttributes(array('endorser' => $dbUser->uid));
				$rUser['endorseReceives'] = Endorsement::model()->findAllByAttributes(array('endorsee' => $dbUser->uid));
				$ret = CJSON::encode($rUser);
			}
		}

		echo $ret;
		Yii::app()->end();
	}

	// Upload a post
	// POST variables:
	// Post['image']		multipart	image
	// Post['description']	string 		description
	// Post['severity']		int
	// Post['lat']			int
	// Post['lng']			int
	// Post['uid']			int
	// Post['category']		int
	// Post['remarks']		string
	public function actionUpload(){
		if(isset($_POST['Post'])){
			$photo=CUploadedFile::getInstanceByName('Post[image]');
			
			if($photo!=null){

				$post=new Post;
				$post->attributes=$_POST['Post'];

				$username="";
				$user=User::model()->findByPk($post->uid);
				if(!empty($user)){
					$username=$user->nickname;
				}

				$transaction = Yii::app()->db->beginTransaction();

				if($post->save()){
					$fileName=md5(time().$post->id).'.jpeg';
					$path='/images/posts/';
					$fullPath=Yii::getPathOfAlias('webroot').$path.$fileName;
					
					if($photo->saveAs($fullPath)){

						$postPhoto=new PostPhoto;
						$postPhoto->pid=$post->id;
						$postPhoto->filename=$fileName;
						$postPhoto->lat=$post->lat;
						$postPhoto->lng=$post->lng;

						if($postPhoto->save()){
							$transaction->commit();
			
							$data=$post->attributes;
							$data['image_file']=$post->photo->filename;
							$data['username']=$username;

							$jsonObj = CJSON::encode($data);
							echo $jsonObj;
						}else{
							//error
							print_r($postPhoto->errors);
							$transaction->rollback();
						}
					}else{
						//rollback
						echo 'Failed to save file';
						$transaction->rollback();
					}
				}else{
					//rollback
					$transaction->rollback();
					print_r($post->errors);
				}
			}else{
				echo 'Photo is empty';
			}
		}else{
			echo 'Post is not set.';
		}
		Yii::app()->end();
	}

	// Gets the list of posts
	// GET paramaters:
	// page_num		int 	page number lor
	public function actionList()
	{
		$page_num=Yii::app()->request->getParam('page_num');
		if(is_numeric($page_num)){
			$criteria=new CDbCriteria;
			$criteria->offset=$page_num*self::LIMIT_PER_PAGE;
			$criteria->limit=self::LIMIT_PER_PAGE;
			$criteria->order='id DESC';
			$posts=Post::model()->with('photo')->findAll($criteria);

			$arr=array();
			$i=0;
			foreach($posts as $post)
			{
				$arr[$i]=$post->attributes;
				$arr[$i]['image_file']=$post->photo->filename;
				if(!empty($post->creator)){
					$arr[$i]['creator']=$post->creator->name;
					$arr[$i]['username']=$post->creator->name;
				}
				$arr[$i]['thumbs'] = $post->thumbs;
				$i++;
			}
			$jasonObj=CJSON::encode($arr);
			echo $jasonObj;
			Yii::app()->end();		
		}
	}

	// Get a particular post
	public function actionGet($id)
	{
		$post=Post::model()->with('photo')->findByPk($id);
		
		$jasonObj=CJSON::encode($post);
		echo $jasonObj;
		Yii::app()->end();		
	}

	// Get a particular user
	public function actionGetUser($uid)
	{
		$user = User::model()->findByPk($uid);

		$jsonObj = CJSON::encode($user);
		echo $jsonObj;
		Yii::app()->end();
	}

	// Submit an endorsement
	// POST variables:
	// Endorsement['endorser']			int 		uid of endorser
	// Endorsement['endorsee']	 		int 		uid of endorsee
	public function actionEndorse()
	{
		$ret = -1;
		if(isset($_POST['Endorsement'])){
			
			$endorsement = Endorsement::model()->findByPk(array('endorser' => $_POST['Endorsement']['endorser'],
																'endorsee' => $_POST['Endorsement']['endorsee']));
			if (!$endorsement) $endorsement = new Endorsement;

			$endorsement->setAttributes($_POST['Endorsement']);

			$transaction = Yii::app()->db->beginTransaction();

			if ($endorsement->save()) {

				$ret = CJSON::encode($endorsement);

				/*
				// ----------------------------------------
				// Update endorsement values - IP, LP
				// ----------------------------------------

				$endorser = $endorsement->endorserUser;
				// IP update
				$neout = count($endorser->endorseeEntries);
				if ($neout == 0) {
					$endorser->ip = 0;
				} else {
					$endorser->ip = floatval($endorser->cp + $endorser->ep) / $neout;
				}

				$endorsee = $endorsement->endorseeUser;
				// LP update
				// Select all users whose endorseeEntries contain an endorsement to the contributor
				$endorsers = User::model()->with(array(
					'endorseeEntries' => array(
						'select' => false,
						'joinType' => 'INNER JOIN',
						'condition' => 'endorseeEntries.endorsee = ' . $endorsee->uid,
					),
				))->findAll();

				$endorsee->lp = 0;
				foreach ($endorsers as $endorser)
				{
					$endorsee->lp += $endorser->ip;
				}

				try {
					$endorser->save();				
					$endorsee->save();
					$transaction->commit();
				} catch (Exception $e) {
					$transaction->rollback();
				}
				*/
			} else {
				$transaction->rollback();

				$ret = CJSON::encode("ERROR");
			}
		}

		echo $ret;
		Yii::app()->end();
	}

	// Submit an thumbs
	// POST variables:
	// Thumbs['postid']	
	// Thumbs['uid']
	// Thumbs['value']
	public function actionThumbs()
	{
		$ret = -1;
		if(isset($_POST['Thumbs'])){
			
			$thumbs = Thumbs::model()->findByPk(array('postid' => $_POST['Thumbs']['postid'],
													'uid' => $_POST['Thumbs']['uid']));
			if (!$thumbs) $thumbs = new Thumbs;

			$thumbs->setAttributes($_POST['Thumbs']);

			$transaction = Yii::app()->db->beginTransaction();

			if ($thumbs->save()) {
				$transaction->commit();
				$ret = CJSON::encode($thumbs);
			} else {
				$transaction->rollback();
				$ret = CJSON::encode("ERROR");
			}
		}

		echo $ret;
		Yii::app()->end();
	}

	// Get an endorsement
	public function getEndorsement($endorser, $endorsee)
	{
		$ret = -1;
		$endorsement = Endorsement::model()->findByPk(array('endorser' => $endorser,
															'endorsee' => $endorsee));
		if ($endorsement) {
			$ret=CJSON::encode($endorsement);
		} else {
			// Nothing
		}
		
		echo $ret;
		Yii::app()->end();
	}


	public function actionAdd()
	{
	}

}
?>
