<?php
$this->pageTitle=Yii::app()->name . ' - Error';
?>


<div class="alert alert-error">
	<h2 class="alert-heading">Error <?php echo $code; ?></h2>
	<p>	
		<?php echo CHtml::encode($message); ?>
	</p>
</div>
