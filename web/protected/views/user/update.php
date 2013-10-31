<?php
$this->pageTitle=Yii::app()->name.' '.Yii::t('translation','Update');
?>

<div class="user-update">
	<?php $form=$this->beginWidget('CActiveForm', array(
	'id'=>'user-update',
	'enableAjaxValidation'=>true,
	'htmlOptions'=>array('class'=>'alert alert-info form-horizontal'),
	)); ?>
		<fieldset>
			<legend><?php echo Yii::t('translation','Modify Profile');?></legend>

				<p class="note">Fields with <span class="required">*</span> are required.</p>

				<?php echo $form->errorSummary($user);?>

				<div class="control-group">
					<?php echo $form->labelEx($user,'email', array('class'=>'control-label')); ?>
					<div class="controls">
						<div class="input-prepend">
							<span class="add-on"><i class="icon-envelope"></i></span>
							<input class="disabled"  type="text" value=<?php echo $user->email;?> disabled  />
						</div>
					</div>
				</div>
		
				<div class="control-group">
					<?php echo $form->labelEx($user,'avatarid', array('class'=>'control-label')); ?>
					<div class="controls">	
						<?php echo CHtml::image('/images/avatar/'.$user->avatar->filename,'', array('id'=>'user-avatar-image', 'width'=>80, 'height'=>80));?>
						<hr class="space"/>
						<a id="changeAvatar" class="btn btn-primary"><?php echo Yii::t('translation','Change avatar');?></a>
					</div>
				</div>
				
				<hr/>

				<div class="control-group">
					<?php echo $form->labelEx($user,'nickname', array('class'=>'control-label')); ?>
					<div class="controls">
						<div class="input-prepend">
							<span class="add-on"><i class="icon-user"></i></span>
							<?php echo $form->textField($user,'nickname',array('size'=>20,'maxlength'=>20)); ?>
							<?php echo $form->error($user,'nickname'); ?>
						</div>
					</div>
				</div>
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
							<?php echo $form->error($user,'sex'); ?>
						</div>
					</div>
				</div>

				<div class="control-group">
					<?php echo $form->labelEx($user,'hp_number', array('class'=>'control-label')); ?>
					<div class="controls">
						<div class="input-prepend">
							<span class="add-on"><?php echo Yii::app()->params['countryCode'];?></span>
							<?php echo $form->textField($user,'hp_number',array('size'=>15,'maxlength'=>15)); ?>
							<?php echo $form->error($user,'hp_number'); ?>
						</div>
					</div>
				</div>


				<?php if(Yii::app()->user->hasFlash('update-success')):?>
					<div class="alert alert-success">
						<p><?php echo Yii::t('Your profile updated');?></p>
					</div>
				<?php endif;?>
			
				<div class="form-actions">
					<?php echo CHtml::submitButton($user->isNewRecord ? Yii::t('translation','Register') : Yii::t('translation','Save'), array('class'=>'btn btn-primary')); ?>
				</div>
		</fieldset>
	<?php $this->endWidget(); ?>
</div>


<div class="modal hide fade" id="changeAvatarModal">
	<?php $form = $this->beginWidget('CActiveForm', array(
			'id'=>'message-form',
			'enableAjaxValidation'=>false,
	)); ?>
		<div class="modal-header">
			<?php echo Yii::t('translation','Choose avatar.');?>
			<a class="close" data-dismiss="modal">×</a>
		</div>

		<div class="modal-body">
			<?php 
				$avatars=$user->avatars;
				$condition="type=:type";
				$param=array(":type"=>$user->sex);
				$commonAvatars=Avatar::model()->findAll($condition, $param);
				$avatars=array_merge($avatars, $commonAvatars);
			?>
			<?php foreach($avatars as $avatar):?>
			<div class="span2">
				<?php $checked=""; if($user->avatarid==$avatar->aid){$checked="checked";}?>
				<?php echo CHtml::image('/images/avatar/'.$avatar->filename,'', array('style'=>'margin:10px; 0', 'width'=>80, 'height'=>80));?>
				<input type="radio" name="avatarid" value="<?php echo $avatar->aid;?>" <?php echo $checked;?>/> 
			</div>
			<?php endforeach;?>
		</div>
			<?php $this->widget('ext.eajaxupload.EAjaxUpload', array(
				   'id'=>'uploadFile',
				   'config'=>array(
						   'action'=>'/user/upload',
						   'allowedExtensions'=>array("jpg","jpeg", "png"),//array("jpg","jpeg","gif","exe","mov" and etc...
						   'sizeLimit'=>1*1024*1024,// maximum file size in bytes
						   'minSizeLimit'=>10*1024,// minimum file size in bytes
						   'onComplete'=>"js:function(id, filename, responseJSON){if(responseJSON.success){var fileName=responseJSON.filename; var avatarid=responseJSON.avatarid; location.reload(); }}",
					)
				  ));
			?>

		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn"><?php echo Yii::t('translation','Cancel');?></a>
			<input href="#" type="submit" class="btn btn-success" value="<?php echo Yii::t('translation','Confirm');?>"/>
		</div>
	<?php $this->endWidget(); ?>
</div>

<script>
$(function(){
	$('#changeAvatar').click(function(){
		$('#changeAvatarModal').modal();
	});
});
</script>
