<?php

class UserController extends Controller{
	/**
	 * Declares class-based actions.
	 */
	public function actions(){
		return array(
			 'captcha'=>array(
				'class'=>'CCaptchaAction',
				'transparent'=>true,
			   ),
		);
	}

	/**
	 * Specifies the access control rules.
	 * This method is used by the 'accessControl' filter.
	 * @return array access control rules
	 */
	public function accessRules(){
		return array(
			array('allow',  // allow all users to perform 'index' and 'view' actions
				'actions'=>array('view', 'captcha', 'token', 'login', 'register', 'find', 'upload'),
				'users'=>array('*'),
			),
			array('allow', // allow authenticated user to perform 'create' and 'update' actions
				'actions'=>array('friend', 'list', 'logout', 'profile','password','update'),
				'users'=>array('@'),
			),
			array('deny',  // deny all users
				'users'=>array('*'),
			),
		);
	}


	/**
	** Performs the AJAX validation.
	** @param CModel the model to be validated
	*/
	protected function performAjaxValidation($model) {
		if(isset($_POST['ajax'])) {
			echo CActiveForm::validate($model);
			Yii::app()->end();
		}
	}

	/**
	 * Load the user by user id.
	 * @param $uid the user id used to fetch user.
	 */
	protected function loadUser($uid){
		$user=User::model()->findByPk($uid);
		if($user==null){
			throw new CHttpException(404, Yii::t('translation','User cannot found'));
		}
		return $user;
	}

	protected function findUserByEmail($email){
		$condition="email = :email";
		$params=array(":email"=>$email);
		return User::model()->find($condition, $params);
	}


	/**
	 *	Login 
	 */
	public function actionLogin(){
		if(!Yii::app()->user->isGuest){
			throw new CHttpException(404, Yii::t('translation','User login already'));
		}

		$model=new LoginForm;

		// if it is ajax validation request
		if(isset($_POST['ajax']) && $_POST['ajax']==='login-form') {
			echo CActiveForm::validate($model);
			Yii::app()->end();
		}

		if(isset($_POST['LoginForm'])) {
			$model->attributes=$_POST['LoginForm'];
			// validate user input and redirect to the previous page if valid
			if($model->validate() && $model->login()){
				$this->redirect(Yii::app()->user->returnUrl);
			}
		}
		
		$this->render('login', array('model'=>$model)); 
	}

	/**
	 *Logout
	 */
	public function actionLogout(){
		Yii::app()->user->logout();
		$this->redirect(Yii::app()->homeUrl);
	}


	/**
	 * Register 
	 */
	public function actionRegister(){
		if(!Yii::app()->user->isGuest){
			throw new CHttpException(404, Yii::t('translation','User login already'));
		}

		$user=new User('register');

		$this->performAjaxValidation($user);

		if(isset($_POST['User'])){
			$user->attributes=$_POST['User'];

			$user->email=strtolower($user->email);
			$user->role=User::ROLE_MEMBER;

			if($user->validate()){
				$user->password=$user->hashPassword($user->password);
				// save user.
				if($user->save(false)){
					//update avatar.
					$avatar=Avatar::model()->findByPk($user->avatarid);
					if($avatar){
						$avatar->uid=$user->uid;
						$avatar->update(array('uid'));
					}else{
						//default avatar
						if($user->sex==User::SEX_MALE){
							$user->avatarid=User::SEX_MALE;
						}else{
							$user->avatarid=User::SEX_FEMALE;
						}
						$user->update(array('avatarid'));
					}

					Yii::app()->user->setFlash('register-success', '');
					$this->refresh();
					//send email
					/*
					$email=$user->email;
					Yii::app()->mailer->getView('register', array('user'=>$user), 'layout');
					$ret=Yii::app()->mailer->CSend(Yii::t('translation','Welcome to register '.Yii::app()->name), $email);
					
					if($ret){
						Yii::app()->user->setFlash('register-success', '');
						$this->refresh();
					}else{
						throw new CHttpException(404, Yii::t('translation','Failed to send email.'));
					}
					*/
				}
				$user->password='';
				$user->password2='';
			}
		}

		$this->render('register', array('user'=>$user)); 
	}	

	/**
	* Upload
	*/
	public function actionUpload(){
		Yii::import("ext.eajaxupload.qqFileUploader");


		$folder='images/avatar/';// folder for uploaded files
		$allowedExtensions = array("jpg", "jpeg", "png");//array("jpg","jpeg","gif","exe","mov" and etc...
		$sizeLimit = 1 * 1024 * 1024;// maximum file size in bytes
		$uploader = new qqFileUploader($allowedExtensions, $sizeLimit);
	
		$result = $uploader->handleUpload($folder);

		$fileName=$result['filename'];//GETTING FILE NAME
		list($width, $height, $type, $attr) = getimagesize($folder.$result['filename']);

		$avatar=new Avatar;
		$avatar->type=Avatar::TYPE_OTHER;
		$avatar->filename=$fileName;
		$avatar->image_h=$height;
		$avatar->image_w=$width;

		if(!Yii::app()->user->isGuest){
			$avatar->uid=Yii::app()->user->id;
		}

		if($avatar->save()){
			$result['avatarid']=$avatar->aid;
			$return = htmlspecialchars(json_encode($result), ENT_NOQUOTES);
			echo $return;// it's array
		}

		Yii::app()->end();
	}


	/**
	* Update
	*/
	public function actionUpdate(){
		$user=$this->loadUser(Yii::app()->user->id);
		$this->layout="//layouts/column2";
		$user->scenario='update';

		$this->performAjaxValidation($user);

		//update user info
		if(isset($_POST['User'])) {
			$user->attributes=$_POST['User'];
			
			if($user->validate()){
				//update avatar.
				$avatar=$user->avatar;
				if($avatar && $avatar->aid>100 && $avatar->uid!=$user->uid){
					$avatar->uid=$user->uid;
					$avatar->update(array('uid'));
				}
				
				if($user->update()){
					Yii::app()->user->setFlash('update-success', '');
					$this->refresh();
				}
			}
		}

		//update user avatar
		if(isset($_POST['avatarid'])){
			$aid=$_POST['avatarid'];
			$avatar=Avatar::model()->findByPk($aid);
			if($avatar==null){
				throw new CHttpException(404, 'Cannot find avatar');
			}
			$user->avatarid=$aid;
			if($user->update(array('avatarid'))){
					Yii::app()->user->setFlash('update-success', '');
					$this->refresh();
			}
		}

		$this->render('update', array('user'=>$user)); 
	}


	/**
	* Password
	*/
	public function actionPassword(){
		$user=new User('password');
		$this->performAjaxValidation($user);
		$this->layout="//layouts/column2";

		if(isset($_POST['User'])){
			$user->attributes=$_POST['User'];


			if($user->validate()){
				$theUser=$this->loadUser(Yii::app()->user->id);

				if($theUser->password===$user->hashPassword($user->oldpassword)){
					$theUser->password=User::hashPassword($user->password);
					$theUser->update(array('password'));
					Yii::app()->user->setFlash('password-success', '');
				}else{
					Yii::app()->user->setFlash('password-wrong', '');
				}
				$this->refresh();
			}
		}

		$this->render('password', array('user'=>$user)); 
	}


	/**
	* Find
	*/
	public function actionFind(){
		$user=new User('find');
		$this->performAjaxValidation($user);

		if($_POST['User']){
			$user->attributes=$_POST['User'];
			if($user->validate()){
				$theUser=$this->findUserByEmail($user->email);
				if($theUser){
					$token=$theUser->email.$theUser->password.time();
					$token=md5($token);
					$encodeToken=base64_encode($token);


					$transaction = Yii::app()->db->beginTransaction();

					try{
						$theUser->token_time=time();
						$theUser->token=$token;
						$theUser->update(array('token', 'token_time'));

						$email=$theUser->email;
						Yii::app()->mailer->getView('find-password', array('user'=>$theUser, 'token'=>$encodeToken), 'layout');
						$ret= Yii::app()->mailer->CSend(Yii::t('translation', 'Retrieve password'.Yii::app()->name), $email);

						if($ret){
							 $transaction->commit();
							 Yii::app()->user->setFlash('find-success','');
							 $this->refresh();
						}else{
							 Yii::app()->user->setFlash('find-fail','');
							 $this->refresh();
						}
					}catch(CException $e){
						 $transaction->rollBack();

						 Yii::app()->user->setFlash('find-fail','');
						 $this->refresh();
					}
				}else{
					Yii::app()->user->setFlash('find-no-email','');
					$this->refresh();
				}	
			}		
		}

		$this->render('find', array('user'=>$user)); 
	}

	/**
	* Token
	*/
	public function actionToken(){
		$token=Yii::app()->request->getQuery('t');
		
		$token=base64_decode($token);
		$condition="token=:token";
		$params=array(":token"=>$token);
		$theUser=User::model()->find($condition, $params);

		if($theUser){
			$user=new User('password');
			$this->performAjaxValidation($user);
			if(isset($_POST['User'])){
				$user->attributes=$_POST['User'];

				if($user->validate()){
					$theUser->password=User::hashPassword($user->password);
					$theUser->update(array('password'));
					Yii::app()->user->setFlash('password-success', '');
					$this->refresh();
				}
			}
			$this->render('find-token', array('user'=>$user)); 
		}else{
			throw new CHttpException(404, 'Token not found');
		}
	}

	/**
	* View
	*/
	public function actionView($id){
		$user=$this->loadUser($id);

		if (!Yii::app()->user->isGuest){
			$fuid=Yii::app()->user->getId();

			$message=new Message;
			$friend=Friend::findModel($fuid, $user->uid);
			if(!$friend){
				$friend=new Friend;
			}

			//send message
		    if(Yii::app()->request->getPost('Message')) {
				$message->attributes = Yii::app()->request->getPost('Message');
				$message->sender_id = $fuid;

				if ($message->sender_id!=$message->receiver_id && $message->save()) {
					Yii::app()->user->setFlash('message-success', 'sent');
					$this->refresh();
				}else{
					Yii::app()->user->setFlash('message-fail', 'sent');
					$this->refresh();
				}
		    }

			//add friend
			if(Yii::app()->request->getPost('Friend')){
				$friend->attributes = Yii::app()->request->getPost('Friend');
				$friend->fuid=$fuid;
				$friend->tuid=$user->uid;

				if($friend->isNewRecord){
					if($friend->save()){
					//send email
				}
				}else{
					$friend->update(array('status'));
				}
				$this->refresh();
			}	
		}else{
			throw new CHttpException(403, 'Please login to view.');
		}	

		$friends=$user->getFriends();

		$this->render('view', array('user'=>$user, 'message'=>$message, 'friend'=>$friend, 'friends'=>$friends)); 
	}

	/**
	* Friend
	*/
	public function actionFriend(){
		$user=$this->loadUser(Yii::app()->user->id);
		$this->layout='/layouts/column2';

		$this->render('friend', array('user'=>$user)); 
	}

	/**
	* List
	*/
	public function actionList(){
		$dataProvider=new CActiveDataProvider('User', array(
			'criteria'=>array(
			),
			'pagination'=>array(
				'pageSize'=>30,
			),
		));

		$this->render('list', array('dataProvider'=>$dataProvider)); 
	}

}
