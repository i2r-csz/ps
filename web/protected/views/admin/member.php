<?php
$this->pageTitle=Yii::app()->name.' '.Yii::t('translation','Member Management');

Yii::app()->clientScript->registerScript('search', "
$('.search-button').click(function(){
		$('.search-form').toggle();
			return false;
		});
$('.search-form form').submit(function(){
		$.fn.yiiGridView.update('user-grid', {
					data: $(this).serialize()
							});
	return false;
});
");
?>

<div class="admin-member">
	<div class="alert alert-info">
		<div class="alert-heading"><?php echo Yii::t('translation','Member');?></div>

		<?php $this->widget('zii.widgets.grid.CGridView', array(
			'id'=>'user-grid',
			'dataProvider'=>$model->search(),
			'filter'=>$model,
			'columns'=>array(
				array(
					'name'=>'uid',
					'type'=>'raw',
					'value'=>'CHtml::link($data->uid, "/user/$data->uid.html",array("target"=>"_blank"))',
					'htmlOptions'=>array('width'=>'30px'),
				),
				array(
					'name'=>'name',
					'type'=>'raw',
					'value'=>'CHtml::link($data->name, "/user/$data->uid.html",array("target"=>"_blank"))',
				),
				array(
					'name'=>'sex',
					'value'=>'Lookup::item("user-sex", $data->sex)',
					'htmlOptions'=>array('width'=>'20'),
				),
				'email',
				'birthday',
				'hp_number',
				'created_on',
			),
		)); ?>
	</div>
</div>
