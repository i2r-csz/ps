<?php
$this->pageTitle=Yii::app()->name.' '.Yii::t('transalatin', 'Find password');
?>

<div class="user-find">
	<?php if(Yii::app()->user->hasFlash('find-success')):?>
	  <div class="alert alert-success">
		<?php echo Yii::t('transalatin', 'Email is sent to you, please check and reset your password.');?>
	  </div>
	<?php elseif(Yii::app()->user->hasFlash('find-fail')):?>
	  <div class="alert alert-error">
		<?php echo Yii::t('transalatin', 'Failed to send you email, please try again later.');?>
	  </div>
	<?php elseif(Yii::app()->user->hasFlash('find-no-email')):?>
	  <div class="alert alert-error">
		<?php echo Yii::t('transalatin', 'Your email is not in our database.');?>
	  </div>
	<?php else: ?>
		<div class="alert alert-info">
			<?php $form=$this->beginWidget('CActiveForm', array(
				'id'=>'user-find',
				'enableAjaxValidation'=>true,
				'clientOptions'=>array(
					'validateOnSubmit'=>true,
				),
				'htmlOptions'=>array('enctype'=>'multipart/form-data', 'class'=>'form-horizontal'),
			)); ?>
				<div class="control-group">
					<?php echo $form->labelEx($user,'email', array('class'=>'control-label')); ?>
					<div class="controls">
						<div class="input-prepend">
							<span class="add-on"><i class="icon-envelope"></i></span>
							<?php echo $form->textField($user,'email',array('class'=>'span3', 'maxlength'=>128)); ?>
							<?php echo $form->error($user,'email'); ?>
						</div>
					</div>
				</div>

				<div class="form-actions">
				<button type="submit" class="btn btn-success" name="confirm"><?php echo Yii::t('translation', 'Reset password');?></button>
				</div>
			<?php $this->endWidget(); ?>
		</div>
	<?php endif;?>
</div>
