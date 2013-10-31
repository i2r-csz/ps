<div class="mail-register">
	<h3>hello, <?php $user->name;?></h3>
	<p>Thank you for registering <?php echo Yii::app()->name;?><p>
	<br/>
	<table>
		<tr>
			<td>Email:</td>
			<td><?php echo $user->email;?></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td>******</td>
		</tr>
	</table>
	<br/>
</div>
