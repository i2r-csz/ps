<?php $form=$this->beginWidget('CActiveForm', array(
	'id'=>'user-register',
	'enableAjaxValidation'=>true,
	'clientOptions'=>array(
		'validateOnSubmit'=>true,
	),
	'htmlOptions'=>array('class'=>'alert alert-info form-horizontal'),
)); ?>
<fieldset>
	<legend><?php echo Yii::t('translation','Welcome to register').' '.Yii::app()->name;?></legend>

		<p class="note">Fields with <span class="required">*</span> are required.</p>

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

		<div class="control-group">
			<?php echo $form->labelEx($user,'password', array('class'=>'control-label')); ?>
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"><i class="icon-lock"></i></span>
					<?php echo $form->passwordField($user,'password',array('class'=>'span3','maxlength'=>128)); ?>
					<?php echo $form->error($user,'password'); ?>
				</div>
			</div>
		</div>

		<div class="control-group">
			<?php echo $form->labelEx($user,'password2', array('class'=>'control-label')); ?>
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"><i class="icon-lock"></i></span>
					<?php echo $form->passwordField($user,'password2',array('class'=>'span3','maxlength'=>128)); ?>
					<?php echo $form->error($user,'password2'); ?>
				</div>
			</div>
		</div>
		
		<hr/>

		<div class="control-group">
			<?php echo $form->labelEx($user,'name', array('class'=>'control-label')); ?>
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"><i class="icon-user"></i></span>
					<?php echo $form->textField($user,'name',array('size'=>20,'maxlength'=>20)); ?>
					<?php echo $form->error($user,'name'); ?>
				</div>
			</div>
		</div>

		<div class="control-group">
			<?php echo $form->labelEx($user,'avatarid', array('class'=>'control-label')); ?>
			<div class="controls">	
				<?php echo CHtml::activeHiddenField($user, 'avatarid', array('id'=>'user-avatarid')); ?>
				<?php echo CHtml::activeHiddenField($user, 'image', array('id'=>'user-avatar-image')); ?>
				<?php if(!$user->avatarid):?>
					<?php $this->widget('ext.eajaxupload.EAjaxUpload', array(
						   'id'=>'uploadFile',
						   'config'=>array(
								   'action'=>'/user/upload',
								   'allowedExtensions'=>array("jpg","jpeg", "png"),//array("jpg","jpeg","gif","exe","mov" and etc...
								   'sizeLimit'=>1*1024*1024,// maximum file size in bytes
								   'minSizeLimit'=>10*1024,// minimum file size in bytes
								   'onComplete'=>"js:function(id, filename, responseJSON){if(responseJSON.success){var fileName=responseJSON.filename; var avatarid=responseJSON.avatarid; $('#user-avatarid').val(avatarid); $('#user-avatar-image').val(fileName); $('#avatar').attr('src', '/images/avatar/'+fileName );}}",
							)
						  ));
					?>
				<?php else:?>
					<?php echo CHtml::image('/images/avatar/'.$user->image,'', array('width'=>80, 'height'=>80));?>
				<?php endif;?>
			</div>
		</div>

		<div class="control-group">
			<?php echo $form->labelEx($user,'birthday', array('class'=>'control-label')); ?>
				<div class="controls">
					<div class="input-prepend">
						<span class="add-on"><i class="icon-calendar"></i></span>
						<?php $this->widget('zii.widgets.jui.CJuiDatePicker', array(
							'model'=>$user, 'attribute'=>'birthday',
							'options'=>array(
								'dateFormat'=>'yy-mm-dd',
								'yearRange'=>'-70:+0',
								'changeYear'=>'true',
								'changeMonth'=>'true',
								'maxDate'=>'-18y',
							),
							'htmlOptions'=>array('class'=>''),
						));?>
						<?php echo $form->error($user,'birthday'); ?>
					</div>
				</div>
		</div>

		<div class="control-group">
			<?php echo $form->labelEx($user,'sex', array('class'=>'control-label')); ?>
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"><i class="icon-user"></i></span>
					<?php echo $form->dropDownList($user,'sex',Lookup::items('user-sex'), array()); ?>
					<?php //echo $form->error($user,'sex'); ?>
				</div>
			</div>
		</div>

		<div class="control-group">
			<?php echo $form->labelEx($user,'hp_number', array('class'=>'control-label')); ?>
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"></span>
					<?php echo $form->textField($user,'hp_number',array('size'=>15,'maxlength'=>15)); ?>
					<?php echo $form->error($user,'hp_number'); ?>
				</div>
			</div>
		</div>

	
		<div class="form-actions">
			<?php echo CHtml::submitButton($user->isNewRecord ? Yii::t('translation','Submit') : Yii::t('translation','Save'), array('class'=>'btn btn-primary')); ?>
		</div>
</fieldset>
<?php $this->endWidget(); ?>
