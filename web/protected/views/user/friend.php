<?php
$this->pageTitle=Yii::app()->name.' '.Yii::t('translation','Friend');
?>
<div class="user-friend">
	<div class="well">
		<h4 class="alert-heading"><?php echo Yii::t('translation','Requests');?><h4>
		<br/>
		<?php foreach($user->pendingFriends as $pendingFriend):?>
			<?php $this->renderPartial('_friend', array('data'=>$pendingFriend));?>
		<?php endforeach;?>
		<div class="clear"></div>
	</div>
	<div class="well">
		<h4 class="alert-heading"><?php echo Yii::t('translation','Friends');?><h4>
		<br/>
		<?php foreach($user->getFriends() as $friend):?>
			<?php $this->renderPartial('_friend', array('data'=>$friend));?>
			<div class="clear"></div>
		<?php endforeach;?>
	</div>
</div>
