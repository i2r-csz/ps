<?php
	$this->pageTitle=Yii::app()->name.' '.Yii::t('translation', 'Change Password');
?>
<div class="user-password">
	<?php if(Yii::app()->user->hasFlash('password-success')):?>
		<div class="alert alert-success">
			<a class="close" data-dismiss="alert">×</a>
			<h4 class="alert-heading"><?php echo Yii::t('translation','Success');?></h4>
			<p><?php echo Yii::t('translation','Your password is changed successfully');?></p>
		</div>
	<?php else:?>
		<?php $form=$this->beginWidget('CActiveForm', array(
					'id'=>'user-password',
					'enableAjaxValidation'=>true,
					'htmlOptions'=>array('class'=>'form-horizontal alert alert-info'),
					)); ?>
		<fieldset>
			<legend><?php echo Yii::t('translation','Change Password');?></legend>

			<?php if(Yii::app()->user->hasFlash('password-wrong')):?>
				<div class="alert alert-error">
					<a class="close" data-dismiss="alert">×</a>
					<h4 class="alert-heading"><?php echo Yii::t('translation','Wrong password');?></h4>
					<p><?php echo Yii::t('translation','Your password is not changed');?></p>
				</div>
			<?php endif;?>

			<div class="control-group">
				<?php echo $form->labelEx($user,'oldpassword', array('class'=>'control-label')); ?>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-lock"></i></span>
						<?php echo $form->passwordField($user,'oldpassword',array('style'=>'margin-left: -4px;','class'=>'span3','maxlength'=>128)); ?>
						<?php echo $form->error($user,'oldpassword'); ?>
					</div>
				</div>
			</div>
			
			<hr/>
			
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
				<input id="confirm" name="confirm" value="<?php echo Yii::t('translation','Confirm');?>" type="submit" class="btn btn-primary"/>
			</div>

		</fieldset>
		<?php $this->endWidget(); ?>

	<?php endif;?>
</div>
