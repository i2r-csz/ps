<?php
$this->pageTitle=Yii::app()->name.' '.UserModule::t('Find Password');
?>
<div class="find-token">
	<?php if(Yii::app()->user->hasFlash('password-success')):?>
		<div class="alert alert-success">
			<a class="close" data-dismiss="alert">×</a>
			<h4 class="alert-heading"><?php echo UserModule::t('Success');?></h4>
			<p><?php echo UserModule::t('Your password is changed successfully');?></p>
		</div>
	<?php else:?>
		<?php $form=$this->beginWidget('CActiveForm', array(
					'id'=>'user-password',
					'enableAjaxValidation'=>true,
					'htmlOptions'=>array('class'=>'form-horizontal alert alert-info'),
					)); ?>
		<fieldset>
			<legend><?php echo UserModule::t('Reset Password');?></legend>

			<?php echo $form->errorSummary($user); ?>

			<?php echo $form->hiddenField($user, 'oldpassword', array('value'=>'xxxxxx'));?>
			<div class="control-group">
				<?php echo $form->labelEx($user,'password', array('class'=>'control-label')); ?>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-lock"></i></span>
						<?php echo $form->passwordField($user,'password',array('style'=>'margin-left: -4px;','class'=>'span3','maxlength'=>128)); ?>
						<?php echo $form->error($user,'password'); ?>
					</div>
				</div>
			</div>

			<div class="control-group">
				<?php echo $form->labelEx($user,'password2',array('class'=>'control-label')); ?>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-lock"></i></span>
						<?php echo $form->passwordField($user,'password2',array('style'=>'margin-left: -4px;','class'=>'span3','maxlength'=>128)); ?>
						<?php echo $form->error($user,'password2'); ?>
					</div>
				</div>
			</div>

			<div class="form-actions">
				<input id="confirm" name="confirm" value="<?php echo UserModule::t('Confirm');?>" type="submit" class="btn btn-primary"/>
			</div>

		</fieldset>
		<?php $this->endWidget(); ?>

	<?php endif;?>
</div>
