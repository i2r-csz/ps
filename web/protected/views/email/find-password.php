<div class="mail-find">
	<h3>hello, <?php $user->nickname;?></h3>
	<br/>
	<p>Please click the link below to reset your password.</p>
	<br/>
	<br/>
	<?php $url=$this->createAbsoluteUrl('/user/find/token/', array('t'=>$token));?>
	<p><?php echo CHtml::link($url, $url);?></p>
	<br/>
	<br/>
	
	
</div>
