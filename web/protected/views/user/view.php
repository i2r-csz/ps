<?php
	$this->pageTitle=Yii::app()->name.' - '.$user->nickname;
?>

<div class="user-view">
	<div class="row">
		<div class="span3">
			<div class="alert alert-error">
				<?php echo CHtml::image('/images/avatar/'.$user->avatar->filename,$user->nickname, array('class'=>'avatar thumbnail'));?>	
				<hr class="space"/>
				<div class="">
					<?php if($user->sex==User::SEX_MALE):?>
						<img style="margin: -3px 3px 0 0; width: 16px; height: 16px;" src="/images/male-icon.png"/>
					<?php else:?>
						<img style="margin: -3px 3px 0 0; width: 16px; height: 16px;" src="/images/female-icon.png"/>
					<?php endif;?>
					<?php echo $user->nickname;?>
					<?php echo $user->age();?>
				</div>
				<br/>
				<?php if($user->uid != Yii::app()->user->id):?>
					<button id="sendMsgBtn" class="btn btn-mini btn-primary"><?php echo Yii::t('translation','Send message');?></button>

					<hr class="space"/>
					<?php if($friend->status==Friend::STATUS_REQUESTED):?>
						<?php if($friend->fuid==Yii::app()->user->id):?>
							<div class="alert alert-success" ><?php echo Yii::t('translation','Friend requested.');?></div>
						<?php elseif($friend->fuid==$user->uid):?>
							<button id="respondFriend" class="btn btn-mini btn-success" ><?php echo Yii::t('translation','Respond to friend request.');?></button>
						<?php endif;?>
					<?php elseif($friend->status==Friend::STATUS_CONNECTED):?>
						<div class="alert alert-success" ><?php echo Yii::t('translation','Your friend.');?></div>
					<?php else:?>
						<button id="addFriend" class="btn btn-mini btn-primary"><?php echo Yii::t('translation','Add Friend');?></button>
					<?php endif;?>
				<?php endif;?>
				<hr class="space"/>
			</div>
				
			<?php if(Yii::app()->user->hasFlash('message-success')):?>
				<div class="alert alert-success">
					Message sent.
				</div>
			<?php elseif(Yii::app()->user->hasFlash('message-fail')):?>
				<div class="alert alert-fail">
					Message failed to send.
				</div>
			<?php endif;?>

			<div class="alert alert-error">
				<h4 class="alert-heading"><?php echo Yii::t('translation','Contact');?></h4>
				<p><?php echo $user->getAttributeLabel('hp_number');?>: <?php echo $user->hp_number;?></p>
			</div>

			<div class="alert alert-error">
				<h4 class="alert-heading"><?php echo Yii::t('translation','Friends');?></h4>
				<ol class="unstyled">
				<?php foreach($friends as $afriend):?>
					<li class="friend">
						<a href="<?php echo $this->createUrl('/user/'.$afriend->uid);?>" target="_blank">
							<img src="<?php echo User::AVATAR_FOLDER.$afriend->avatar->filename;?>" width=50 height=50/>
						</a>
						<?php //echo $user->nickname;?>
					</li>	
				<?php endforeach;?>
				</ol>
				<div class="clear"></div>
			</div>
		</div>
		<div class="span9">
		</div>
	</div>
</div>

<?php if(!Yii::app()->user->isGuest):?>
	<div class="modal hide fade" id="sendMessageModal">
		<?php $form = $this->beginWidget('CActiveForm', array(
				'id'=>'message-form',
				'enableAjaxValidation'=>false,
		)); ?>
			<div class="modal-header">
				<?php echo Yii::t('translation','Send To');?>: <?php echo $user->nickname;?>
				<a class="close" data-dismiss="modal">×</a>
			</div>

			<div class="modal-body">
				<?php echo $form->errorSummary($message, null, null, array('class' => 'alert alert-error')); ?>

				<div class="input">
					<?php echo $form->hiddenField($message,'receiver_id', array('value'=>$user->uid)); ?>
				</div>

				<?php echo $form->labelEx($message,'body'); ?>
				<div class="input">
					<?php echo $form->textArea($message,'body', array('class'=>'span7', 'rows'=>4)); ?>
					<?php echo $form->error($message,'body'); ?>
				</div>
			</div>

			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn"><?php echo Yii::t('translation','Cancel');?></a>
				<input href="#" type="submit" class="btn btn-success" value="<?php echo Yii::t('translation','Send');?>"/>
			</div>
		<?php $this->endWidget(); ?>
	</div>

	<div class="modal hide fade" id="addFriendModal">
		<?php $form = $this->beginWidget('CActiveForm', array(
				'id'=>'message-form',
				'enableAjaxValidation'=>false,
		)); ?>
			<div class="modal-header">
				Request friend :<?php echo $user->nickname;?>
				<a class="close" data-dismiss="modal">×</a>
			</div>

			<div class="modal-body">
				<?php echo $form->labelEx($friend,'comment'); ?>
				<div class="input">
					<?php echo $form->textArea($friend,'comment', array('class'=>'span7', 'rows'=>4)); ?>
					<?php echo $form->error($friend,'comment'); ?>
				</div>
				<?php echo $form->hiddenField($friend, 'status', array('value'=>Friend::STATUS_REQUESTED));?>
			</div>

			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn"><?php echo Yii::t('translation','Cancel');?></a>
				<input href="#" type="submit" class="btn btn-success" value="<?php echo Yii::t('translation','Confirm');?>"/>
			</div>
		<?php $this->endWidget(); ?>
	</div>

	<div class="modal hide fade" id="respondFriendModal">
		<?php $form = $this->beginWidget('CActiveForm', array(
				'id'=>'message-form',
				'enableAjaxValidation'=>false,
		)); ?>
			<div class="modal-header">
				<?php //echo $user->nickname;?>
				<?php echo Yii::t('translation','Confirm him/her as friend?');?>
				<a class="close" data-dismiss="modal">×</a>
			</div>

			<div class="modal-body">
				<?php echo Yii::t('translation','Request');?>: <?php echo $friend->comment;?>
				<?php echo $form->hiddenField($friend, 'status', array('value'=>Friend::STATUS_CONNECTED));?>
			</div>

			<div class="modal-footer">
				<a href="#" data-dismiss="modal" class="btn"><?php echo Yii::t('translation','Cancel');?></a>
				<input href="#" type="submit" class="btn btn-success" value="<?php echo Yii::t('translation','Confirm');?>"/>
			</div>
		<?php $this->endWidget(); ?>
	</div>

	<script>
	$(function(){
		$('#sendMsgBtn').click(function(){
			$('#sendMessageModal').modal();
		});
		$('#addFriend').click(function(){
			$('#addFriendModal').modal();
		});
		$('#respondFriend').click(function(){
			$('#respondFriendModal').modal();
		});
	});
	</script>
<?php endif;?>
