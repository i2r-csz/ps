<?php
$this->pageTitle=Yii::app()->name.' '.Yii::t('translation','Contact Us');
?>
<div class="admin-contact">
	<div class="alert alert-info">
		<div class="alert-heading"><?php echo Yii::t('translation','Contact Us');?></div>

		<?php $this->widget('zii.widgets.grid.CGridView', array(
			'id'=>'user-grid',
			'dataProvider'=>$model->search(),
			'filter'=>$model,
			'columns'=>array(
				array(
					'name'=>'cid',
					'htmlOptions'=>array('width'=>'40'),
				),
				'contact',
				'content',
				'created_on',
			),
		)); ?>
	</div>
</div>
