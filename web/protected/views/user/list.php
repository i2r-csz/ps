<?php
	$this->pageTitle=Yii::app()->name . ' - '.UserModule::t('User list');
?>
<div class="user-list">
<?php $this->widget('zii.widgets.CListView', array(
	'dataProvider'=>$dataProvider,
	'itemView'=>'/default/_list',
	'ajaxUpdate'=>false, 
	'itemsTagName'=>'div',
	'itemsCssClass'=>'thumbnails',
	'template'=>'{summary}{pager}{items}',
/*	'summaryText'=>"共{count}个网站当前显示从{start}到{end}", 
	'pager'=>array(
		'header'=>"",
		'firstPageLabel'=>'第一页',
		'prevPageLabel'=>'上一页',
		'nextPageLabel'=>'下一页',	
		'lastPageLabel'=>'最后一页',
	)
 */
	)); ?>
<br/>
</div>
