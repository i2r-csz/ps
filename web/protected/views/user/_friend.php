<div class="span1 alert alert-error">
	<a target="_blank" href="<?php echo $this->createUrl('/user/'.$data->uid);?>"><?php echo CHtml::image("/images/avatar/".$data->avatar->filename, $data->nickname, array('style'=>'height:50px; width:50px;'));?></a>
	<h4>
		<?php if($data->sex==User::SEX_MALE):?>
			<img style="margin: -3px 3px 0 0; width: 16px; height: 16px;" src="/images/male-icon.png"/>
		<?php else:?>
			<img style="margin: -3px 3px 0 0; width: 16px; height: 16px;" src="/images/female-icon.png"/>
		<?php endif;?>
		<?php echo CHtml::link($data->nickname, $this->createUrl('/user/'.$data->uid));?>
	</h4>
	<p><?php echo CHtml::link(Yii::t('translation','View more'), array('/user/'.$data->uid), array('class'=>'btn btn-mini btn-danger'));?></p>
</div>

