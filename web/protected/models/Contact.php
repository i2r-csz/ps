<?php

/**
 * This is the model class for table "contact".
 *
 * The followings are the available columns in table 'contact':
 * @property integer $cid
 * @property integer $uid
 * @property string $contact
 * @property string $content
 * @property integer $modified_on
 * @property integer $created_on
 */
class Contact extends CActiveRecord
{
	public $verifyCode;
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Contact the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}

	/**
	 * @return string the associated database table name
	 */
	public function tableName()
	{
		return 'contact';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('contact, content, modified_on, created_on', 'required'),
			array('uid', 'numerical', 'integerOnly'=>true),
			array('contact', 'length', 'max'=>255),
			array('content', 'length', 'max'=>1024),
			array('verifyCode', 'captcha', 'allowEmpty'=>!CCaptcha::checkRequirements()),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('cid, uid, contact, content, modified_on, created_on', 'safe', 'on'=>'search'),
		);
	}

	/**
	 * @return array relational rules.
	 */
	public function relations()
	{
		// NOTE: you may need to adjust the relation name and the related
		// class name for the relations automatically generated below.
		return array(
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'cid' => Yii::t('translation', 'Cid'),
			'uid' => 'Uid',
			'contact' => Yii::t('translation', 'Your Contact'),
			'content' => Yii::t('translation', 'Feedback'),
			'modified_on' => 'Modified On',
			'created_on' => Yii::t('translation', 'Created On'),
		);
	}

	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public function search()
	{
		// Warning: Please modify the following code to remove attributes that
		// should not be searched.

		$criteria=new CDbCriteria;

		$criteria->compare('cid',$this->cid);
		$criteria->compare('uid',$this->uid);
		$criteria->compare('contact',$this->contact,true);
		$criteria->compare('content',$this->content,true);
		$criteria->compare('modified_on',$this->modified_on);
		$criteria->compare('created_on',$this->created_on);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}

	protected function beforeValidate() {
		if($this->getIsNewRecord()){
			$this->created_on = Date('Y-m-d H:i:s');
		}
		
		$this->modified_on = Date('Y-m-d H:i:s');
		return parent::beforeSave();
	}
}
