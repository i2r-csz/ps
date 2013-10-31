<?php

/**
 * This is the model class for table "avatar".
 *
 * The followings are the available columns in table 'avatar':
 * @property integer $aid
 * @property integer $uid
 * @property string $filename
 * @property integer $image_h
 * @property integer $image_w
 * @property integer $created_on
 * @property integer $modified_on
 */
class Avatar extends CActiveRecord
{
	const TYPE_OTHER=0;
	const TYPE_COMMON_MALE=1;
	const TYPE_COMMON_FEMALE=2;
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Avatar the static model class
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
		return 'avatar';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('filename, image_h, image_w, created_on, modified_on', 'required'),
			array('aid, uid, image_h, image_w', 'numerical', 'integerOnly'=>true),
			array('filename', 'length', 'max'=>255),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('aid, uid, filename, image_h, image_w, created_on, modified_on', 'safe', 'on'=>'search'),
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
			'aid' => 'Pid',
			'uid' => 'Uid',
			'filename' => 'Filename',
			'image_h' => 'Image H',
			'image_w' => 'Image W',
			'created_on' => 'Created On',
			'modified_on' => 'Modified On',
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

		$criteria->compare('aid',$this->aid);
		$criteria->compare('uid',$this->uid);
		$criteria->compare('filename',$this->filename,true);
		$criteria->compare('image_h',$this->image_h);
		$criteria->compare('image_w',$this->image_w);
		$criteria->compare('created_on',$this->created_on);
		$criteria->compare('modified_on',$this->modified_on);

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
