
<div class="navbar">
	<div class="navbar-inner">
		<div class="container">
			<?php $this->widget('zii.widgets.CMenu',array(
					'htmlOptions'=>array('class'=>'nav'),
					'items'=>array(
							array('label'=>Yii::t('translation', 'Home'), 'url'=>array('/site/index')),
					),
			)); ?>


			<ul class="nav pull-right">
				<?php if(Yii::app()->user->isGuest):?>
				<li><a class="" href="<?php echo $this->createUrl('/user/login');?>"><i
						class="icon-user"></i> <?php echo Yii::t('translation', 'Login');?>
				</a></li>
				<li><a class=""
					href="<?php echo $this->createUrl('/user/register');?>"><?php echo Yii::t('translation', 'Register');?>
				</a></li>
				<?php else:?>
				<?php 
				$unreadCount=Message::model()->getCountUnreaded(Yii::app()->user->id);
				$unreadLabel="";
				if($unreadCount>0){
							$unreadLabel=' <span class="badge badge-warning">'.$unreadCount.'</span>';
						}
						?>
				<li><a href="<?php echo $this->createUrl('/message');?>"><i
						class="icon-envelope"></i> <?php echo Yii::t('translation', 'Message');?>
						<?php echo $unreadLabel;?> </a></li>
				<li><a class=""
					href="<?php echo $this->createUrl('/user/'.Yii::app()->user->id);?>"><?php echo Yii::t('translation', 'Home Page');?>
				</a></li>
				<li class="dropdown"><a href="#" data-toggle="dropdown"
					class="dropdown-toggle"><?php echo Yii::t('translation', 'Settings');?><b
						class="caret"></b> </a>
					<ul class="dropdown-menu">
						<li><a href="<?php echo $this->createUrl('/user/update');?>"><i
								class="icon-user"></i> <?php echo Yii::t('translation', 'Account');?>
						</a></li>
						<li class="divider"></li>
						<li><a href="<?php echo $this->createUrl('/user/logout');?>"><i
								class="icon-remove"></i> <?php echo Yii::t('translation', 'Logout');?>
						</a></li>
					</ul>
				</li>
				<?php endif;?>
			</ul>
		</div>
	</div>
</div>
