<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="language" content="en" />

<link rel="stylesheet" type="text/css"
	href="<?php echo Yii::app()->request->baseUrl; ?>/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="<?php echo Yii::app()->request->baseUrl; ?>/css/theme.css" />
<link rel="stylesheet" type="text/css"
	href="<?php echo Yii::app()->request->baseUrl; ?>/css/base.css" />
<link rel="stylesheet" type="text/css"
	href="<?php echo Yii::app()->request->baseUrl; ?>/css/main.css" />

<?php Yii::app()->clientScript->registerCoreScript('jquery');?>
<?php 
$bootstrapJs=Yii::app()->assetManager->publish(Yii::getPathOfAlias('application').'/assets/bootstrap.min.js', false, -1, true);
Yii::app()->clientScript->registerScriptFile($bootstrapJs, CClientScript::POS_HEAD);
?>

<title><?php echo CHtml::encode($this->pageTitle); ?></title>
</head>

<body>

	<div id="page">

		<div id="header">
			<?php $this->renderPartial('//layouts/_header');?>
		</div>
		<!-- header -->

		<?php echo $content; ?>

		<div id="footer">
			<?php $this->renderPartial('//layouts/_footer');?>
		</div>
		<!-- footer -->

	</div>
	<!-- page -->

	<script type="text/javascript">
	$('.dropdown-toggle').dropdown();
</script>
</body>
</html>
