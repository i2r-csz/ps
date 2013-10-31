<?php
	$this->pageTitle=Yii::app()->name . ' - '.Yii::t('translation', 'Login');
	$this->breadcrumbs=array(
		Yii::t('translation','Login'),
	);
?>

<div class="user-login">
	<?php $form=$this->beginWidget('CActiveForm', array(
		'id'=>'login-form',
		'enableClientValidation'=>true,
		'clientOptions'=>array(
			'validateOnSubmit'=>true,
		),
		'htmlOptions'=>array('class'=>'alert alert-info form-horizontal'),
	)); ?>
		<fieldset>
			<legend><?php echo Yii::t('translation', 'Login');?></legend>

			<p class="note">Fields with <span class="required">*</span> are required.</p>

			<div class="control-group">
				<?php echo $form->labelEx($model,'email',array('class'=>'control-label')); ?>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-user"></i></span>
						<?php echo $form->textField($model,'email',array()); ?>
						<?php echo $form->error($model,'email'); ?>
					</div>
				</div>
			</div>

			<div class="control-group">
				<?php echo $form->labelEx($model,'password',array('class'=>'control-label')); ?>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-lock"></i></span>
						<?php echo $form->passwordField($model,'password',array()); ?>
						<?php echo $form->error($model,'password'); ?>
					</div>
				</div>
			</div>

			<div class="control-group">
				<?php echo $form->label($model,'rememberMe',array('class'=>'control-label')); ?>
				<div class="controls">
					<?php echo $form->checkBox($model,'rememberMe'); ?>
					<?php echo $form->error($model,'rememberMe'); ?>
				</div>
			</div>

			<?php if(CCaptcha::checkRequirements()): ?>
				<div class="control-group">
					<div class="control-label">
						<?php $this->widget('CCaptcha', array(
							'showRefreshButton'=>false,
							'clickableImage'=>true,
							'imageOptions'=>array(
								'title'=>Yii::t('translation', 'Click to change'),
								'style'=>'cursor:pointer',
							),
						)); ?>
					</div>
					<div class="controls">
						<?php echo $form->textField($model,'verifyCode', array('placeholder'=>Yii::t('translation', 'Enter verify code'))); ?>
						<?php echo $form->error($model,'verifyCode'); ?>
					</div>
				</div>
			<?php endif; ?>

			<div class="form-actions">
				<?php echo CHtml::submitButton(Yii::t('translation', 'Login'), array('class'=>'btn btn-primary')); ?>
			</div>
		</fieldset>

		<div class="">
			<?php echo Yii::t('translation', 'Forget password? Please').' '.CHtml::link(Yii::t('translation', 'Find'), array('/user/find'));?>.<br/>	
			<?php echo Yii::t('translation', 'Don\'t have account? Please').' '.CHtml::link(Yii::t('translation', 'Register'), array('/user/register'));?>.	
		</div>
	<?php $this->endWidget(); ?>

</div><!-- site-login -->
