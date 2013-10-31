<?php

class AdminController extends Controller
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
	 * Specifies the access control rules.
	 * This method is used by the 'accessControl' filter.
	 * @return array access control rules
	 */
	public function accessRules(){
		return array(
			array('allow', // allow admin user to perform 'admin' and 'delete' actions
				'actions'=>array('member', 'contact'),
				'roles'=>array('admin'),
			),
			array('deny',  // deny all users
				'users'=>array('*'),
			),
		);
	}

	/**
	* Member
	*/
	public function actionMember(){
		$model=new User('search');
		$model->unsetAttributes();  // clear any default values
		$this->layout="//layouts/column2";
		if(isset($_GET['User'])){
			$model->attributes=$_GET['User'];
		}
		$this->render('member', array('model'=>$model)); 
	}

	/**
	* Contact
	*/
	public function actionContact(){
		$model=new Contact('search');
		$model->unsetAttributes();  // clear any default values
		$this->layout="//layouts/column2";

		if(isset($_GET['Contact'])){
			$model->attributes=$_GET['Contact'];
		}
		$this->render('contact', array('model'=>$model)); 
	}

}
