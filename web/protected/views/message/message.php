<?php
$this->pageTitle=Yii::app()->name.' '.Yii::t('translation','Message');
?>
<div class="user-message">
	<?php foreach($messages as $message):?>
		<?php $inMsg=$message->receiver->uid==Yii::app()->user->id;
			if($inMsg){
				$user=$message->sender;
			}else{
				$user=$message->receiver;
			}
		?>
		<div class="well">
			<div class="row">
				<div class="span1">
					<a href="<?php $this->createUrl('/user/'.$user->uid);?>" target="_blank">
					<?php echo CHtml::image('/images/avatar/'.$user->avatar->filename, '', array('class'=>'thumbnail', 'width'=>60, 'height'=>60));?>
					</a>
				</div>
				<div class="span6">
					<p><?php if(!$inMsg):?><?php echo Yii::t('translation','Send To');?> <?php endif;?><?php echo CHtml::link($user->nickname,$this->createUrl('/user/'.$user->uid));?>: <?php echo $message->body;?></p>
					<div class="row">
						<div class="span2 timestamp">
							<?php echo $message->created_at.' ('.Lookup::item('user-message', $message->is_read).')';?>
						</div>
						<div class="pull-right">
							<a href="<?php echo $this->createUrl('message/between/', array('uid'=>$user->uid));?>"><span class="label label-success"><?php echo Yii::t('translation','Total');?>: <?php echo $message->totalMessagesCount;?></span></a>
							<?php if($message->unreadMessagesCount > 0):?>
								| <a href="<?php echo $this->createUrl('message/between/', array('uid'=>$user->uid));?>"><span class="label label-important">unread: <?php echo $message->unreadMessagesCount;?></span></a>
							<?php endif;?>
						</div>
					</div>
					
				</div>
			</div>
		</div>
	<?php endforeach;?>
</div>
