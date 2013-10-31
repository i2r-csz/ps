<?php

/**
 * UserIdentity represents the data needed to identity a user.
 * It contains the authentication method that checks if the provided
 * data can identity the user.
 */
class UserIdentity extends CUserIdentity
{
	private $_id;
	private $_name;

	/**
	 * Authenticates a user.
	 * The example implementation makes sure if the username and password
	 * are both 'demo'.
	 * In practical applications, this should be changed to authenticate
	 * against some persistent user identity storage (e.g. database).
	 * @return boolean whether authentication succeeds.
	 */
	public function authenticate()
	{
		$email=strtolower($this->username);
		$password=$this->password;
		$user=User::model()->find('email=?', array($email));

		if($user==null){
			$this->errorCode=self::ERROR_USERNAME_INVALID;
		}else if(!$user->validatePassword($password)){
			$this->errorCode=self::ERROR_PASSWORD_INVALID;
		}else{
			$this->_id=$user->uid;
			$this->_name=$user->name;

			$role=Lookup::item('user-role',$user->role);
			$this->setState('role', $role);

			$auth=Yii::app()->authManager;
			if(!$auth->isAssigned($role, $this->_id)) {
				if($auth->assign($role, $this->_id)) {
					Yii::app()->authManager->save();
				}
			}

			$this->errorCode=self::ERROR_NONE;
		}
		return $this->errorCode==self::ERROR_NONE;

	}

	/**
	 * This method will return user's uid
	 */
	public function getId()
	{
		return $this->_id;
	}
	public function getName(){
		return $this->_name;
	}
}
