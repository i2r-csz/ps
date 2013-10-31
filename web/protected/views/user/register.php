<?php 
	$this->pageTitle=Yii::app()->name . ' - '.Yii::t('translation','Register'); 
	$this->breadcrumbs=array(
		Yii::t('translation','Register'),
	);
?>
<div class="user-register">
	<?php if(Yii::app()->user->hasFlash('register-success')):?>
		<div class="alert alert-success">
			<?php echo Yii::t('translation','Thank you for Register');?>
		</div>
	<?php else: ?>
		<?php echo $this->renderPartial('_register', array('user'=>$user)); ?>
	<?php endif; ?>
</div>
