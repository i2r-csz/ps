<?php
$inMsg=$data->receiver->uid==Yii::app()->user->id;
$user=$data->sender; 

$isread=$data->is_read;
//update unread
if(!$data->is_read && $inMsg){
	$data->markAsRead();
}
?>
<div class="item well">
<?php if($inMsg):?>
	<div class="row">
		<div class="span1">
			<a href="<?php $this->createUrl('/user/'.$user->uid);?>" target="_blank">
			<?php echo CHtml::image('/images/avatar/'.$user->avatar->filename, '', array('class'=>'thumbnail', 'width'=>60, 'height'=>60));?>
			</a>
		</div>
		<div class="span6">
			<p><b><?php echo CHtml::link($user->nickname,$this->createUrl('/user/'.$user->uid));?></b>: <?php echo $data->body;?></p>
			<div class="row">
				<div class="span2 timestamp">
					<?php echo $data->created_at;?>
				</div>
			</div>
			<div class="row">
				<div class="span1">
					<?php if(!$isread):?>
						<span class="label label-important">unread</span>
					<?php endif;?>
				</div>
			</div>
			
		</div>
	</div>
<?php else:?>
	<div class="row">
		<div class="span6">
			<p><b><?php echo $user->nickname;?></b>: <?php echo $data->body;?></p>
			<div class="row">
				<div class="span2 timestamp">
					<?php echo $data->created_at;?>
				</div>
			</div>
			
		</div>
		<div class="span1">
			<a href="<?php $this->createUrl('/user/'.$user->uid);?>" target="_blank">
			<?php echo CHtml::image('/images/avatar/'.$user->avatar->filename, '', array('width'=>60, 'height'=>60, 'class'=>'thumbnail'));?>
			</a>
		</div>
	</div>
<?php endif;?>
</div>
