<?php $this->beginContent('//layouts/main'); ?>

<?php
$message=new Message;
$unreadCount=$message->getCountUnreaded(Yii::app()->user->id);

$unreadLabel='';
if($unreadCount>0){
	$unreadLabel='<span class="badge badge-important">'.$unreadCount.'</span>';
}

$userMenu=array(
	'/message'=>Yii::t('translation','Messages').' '.$unreadLabel,
	'/user/friend'=>Yii::t('translation','Friends'),
	'seprator',
	'/user/update'=>Yii::t('translation','Update'),
	'/user/password'=>Yii::t('translation','Change Password'),
);
$adminMenu=array(
	'/admin/member'=>Yii::t('translation','Member Management'),
	'/admin/contact'=>Yii::t('translation','Contact'),
);

$icons=array(
	'/user/friend'=>'icon-user',
	'/message'=>'icon-envelope',
	'/user/update'=>'icon-edit',
	'/user/password'=>'icon-lock',
	'/admin/member'=>'icon-calendar',
	'/admin/contact'=>'icon-pencil',
);

$curPath=$_SERVER['REQUEST_URI'];
?>

<div class="container">
	<div class="row">	
		<div class="span3 ">
			<div class="alert alert-info" style="padding: 8px 0;">
				<ul class="nav nav-list">
					<?php foreach($userMenu as $path=>$label):?>
						<?php if($path=='separator'):?>
							<li class="divider"></li>
						<?php else:?>
							<?php if(strpos($curPath, $path)!==false):?>
								<li class="active">
							<?php else:?>
								<li>
							<?php endif;?>
							<a href="<?php echo $this->createUrl($path);?>" > <i class="<?php echo $icons[$path];?>"></i> <?php echo $label;?></a>
							</li>
						<?php endif;?>
					<?php endforeach;?>
					<?php if(Yii::app()->user->role==Lookup::item('user-role', User::ROLE_ADMIN)):?>
						<li class="divider"></li>
						<?php foreach($adminMenu as $path=>$label):?>
							<?php if(strpos($curPath, $path)!==false):?>
								<li class="active">
							<?php else:?>
								<li>
							<?php endif;?>
							<a href="<?php echo $this->createUrl($path);?>" > <i class="<?php echo $icons[$path];?>"></i> <?php echo $label;?></a>
							</li>
						<?php endforeach;?>
					<?php endif;?>
				</ul>
			</div>
		</div>
		<div class="span9">
			<?php echo $content; ?>
		</div>
	</div>
</div>
<?php $this->endContent(); ?>
