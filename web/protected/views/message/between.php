<?php 
$this->pageTitle=Yii::app()->name.' Private Message';
?>
<div class="user-message-between">

<?php if(Yii::app()->user->hasFlash('message-success')):?>
	<div class="alert alert-success">
		Message sent.
	</div>
<?php else:?>
	<div class="form">
		<?php $form = $this->beginWidget('CActiveForm', array(
			'id'=>'message-form',
			'enableAjaxValidation'=>false,
		)); ?>
			<div class="input">
				<?php echo $form->textArea($message,'body', array('class'=>'span9', 'rows'=>'4')); ?>
				<?php echo $form->error($message,'body'); ?>
			</div>

			<div class="buttons">
				<button class="btn btn-primary"><?php echo Yii::t('translation',"Send") ?></button>
			</div>
		<?php $this->endWidget(); ?>
	</div>
<?php endif;?>
<?php
$this->widget('zii.widgets.CListView', array(
    'dataProvider'=>$dataProvider,
    'itemView'=>'_message',   // refers to the partial view named '_post'
    'template'=>'{items}',
));
?>

</div>
