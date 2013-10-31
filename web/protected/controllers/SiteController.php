<?php

class SiteController extends Controller
{
	/**
	 * Declares class-based actions.
	 */
	public function actions()
	{
		return array(
			 'captcha'=>array(
				'class'=>'CCaptchaAction',
				'transparent'=>true,
			   ),
		);
	}

	/**
	 * This is the default 'index' action that is invoked
	 * when an action is not explicitly requested by users.
	 */
	public function actionIndex() {
		// renders the view file 'protected/views/site/index.php'
		// using the default layout 'protected/views/layouts/main.php'
		$this->render('index');
	}

	/**
	 * This is the action to handle external exceptions.
	 */
	public function actionError() {
	    if($error=Yii::app()->errorHandler->error)
	    {
	    	if(Yii::app()->request->isAjaxRequest)
	    		echo $error['message'];
	    	else
	        	$this->render('error', $error);
	    }
	}

	public function actionAbout(){
		$this->render('about');
	}

	/**
	 * Displays the contact page
	 */
	public function actionContact() {
		$model=new Contact;
		if(isset($_POST['Contact'])) {
			$model->attributes=$_POST['Contact'];
			if($model->validate()) {
				//
				if(!Yii::app()->user->isGuest){
					$model->uid=Yii::app()->user->id;
				}

				if($model->save()){
					Yii::app()->user->setFlash('contact','');
					$this->refresh();
				}
			}
		}
		$this->render('contact',array('model'=>$model));
	}

}
