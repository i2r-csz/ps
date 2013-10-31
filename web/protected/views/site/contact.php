<?php
$this->pageTitle=Yii::app()->name . ' - '.Yii::t('translation', 'Contact Us');

$this->breadcrumbs=array(
	Yii::t('translation', 'Contact Us'),
);
?>


<div class="site-contact">
	<?php if(Yii::app()->user->hasFlash('contact')): ?>
		<div class="alert alert-success">
			Thank you, we will contact you.
		</div>
	<?php else: ?>
		<?php $form=$this->beginWidget('CActiveForm', array(
			'id'=>'contact-form',
			'enableClientValidation'=>true,
			'clientOptions'=>array(
				'validateOnSubmit'=>true,
			),
			'htmlOptions'=>array('class'=>'alert alert-info form-horizontal'),
		)); ?>

			<legend><?php echo Yii::t('translation', 'Contact Us');?></legend>

			<p class="note">Fields with <span class="required">*</span> are required.</p>

			<?php echo $form->errorSummary($model); ?>

			<div class="control-group">
				<?php echo $form->labelEx($model,'content', array('class'=>'control-label')); ?>
				<div class="controls">
					<?php $this->widget('ext.kindeditor.KindEditor',
						array(
							'model'=>$model,
							'attribute'=>'content',
						)
					); ?>
					<?php echo $form->textArea($model,'content',array('rows'=>5, 'class'=>'span5', 'maxlength'=>1024)); ?>
					<?php echo $form->error($model,'content'); ?>
				</div>
			</div>

			<div class="control-group">
				<?php echo $form->labelEx($model,'contact', array('class'=>'control-label')); ?>
				<div class="controls">
					<?php echo $form->textField($model,'contact',array('class'=>'span3', 'maxlength'=>128)); ?>
					<?php echo $form->error($model,'contact'); ?>
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
				<?php echo CHtml::submitButton(Yii::t('translation','Submit'),array('class'=>'btn btn-primary')); ?>
			</div>

		<?php $this->endWidget(); ?>
	<?php endif; ?>
</div>
