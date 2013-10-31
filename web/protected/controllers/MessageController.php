<?php

class MessageController extends Controller{
	public $layout='/layouts/column2';

	public function actionIndex(){
		$dataProvider=Message::getAdapter(Yii::app()->user->id);

		$messages=array();
		foreach($dataProvider->getData() as $message){
			$sid=$message->sender_id;
			$rid=$message->receiver_id;
			
			$fMessage=$this->findMessage($messages, $sid, $rid);
			if(!$fMessage){
				$messages[]=$message;	
				$fMessage=$message;
			}

			if(!$message->is_read && $rid==Yii::app()->user->id){
				$fMessage->unreadMessagesCount++;
			}
			$fMessage->totalMessagesCount++;
		}

		$this->render('message', array('messages'=>$messages)); 
	}

	private function findMessage($messages, $sid, $rid){
		foreach($messages as $message){
			if(($message->receiver_id==$rid && $message->sender_id==$sid) ||
			   ($message->sender_id==$rid && $message->receiver_id==$sid)){
				   return $message;
			   }
		}
		return null;
	}

	public function actionBetween(){
		$uid=$_GET['uid'];
		$user=User::model()->findByPk($uid);
		if($user==null){
			throw new CHttpException(404, 'User Not Found');
		}
		if($uid==Yii::app()->user->id){
			throw new CHttpException(404, 'Error');
		}
	
		$message=new Message;
		if($_POST['Message']){
			$message->sender_id=Yii::app()->user->id;
			$message->receiver_id=$uid;
			$message->attributes=$_POST['Message'];
			if($message->validate()){
				$message->save();
				Yii::app()->user->setFlash('message-success','');
				$this->refresh();
			}
		}

		$dataProvider=Message::getAdapterForBetween($user->uid, Yii::app()->user->id);
		$this->render('between', array('dataProvider'=>$dataProvider,'message'=>$message)); 
	}
}
