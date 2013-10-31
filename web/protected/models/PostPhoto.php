<?php

/**
 * This is the model class for table "post_photo".
 *
 * The followings are the available columns in table 'post_photo':
 * @property string $ppid
 * @property string $pid
 * @property string $filename
 * @property double $lat
 * @property double $lng
 * @property double $accuracy
 * @property string $modified_on
 * @property string $created_on
 */
class PostPhoto extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return PostPhoto the static model class
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
		return 'post_photo';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('pid, filename, lat, lng, modified_on, created_on', 'required'),
			array('lat, lng, accuracy', 'numerical'),
			array('pid', 'length', 'max'=>20),
			array('filename', 'length', 'max'=>1024),
			array('created_on', 'safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('ppid, pid, filename, lat, lng, accuracy, modified_on, created_on', 'safe', 'on'=>'search'),
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
			'ppid' => 'Ppid',
			'pid' => 'Pid',
			'filename' => 'Filename',
			'lat' => 'Lat',
			'lng' => 'Lng',
			'accuracy' => 'Accuracy',
			'modified_on' => 'Modified On',
			'created_on' => 'Created On',
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

		$criteria->compare('ppid',$this->ppid,true);
		$criteria->compare('pid',$this->pid,true);
		$criteria->compare('filename',$this->filename,true);
		$criteria->compare('lat',$this->lat);
		$criteria->compare('lng',$this->lng);
		$criteria->compare('accuracy',$this->accuracy);
		$criteria->compare('modified_on',$this->modified_on,true);
		$criteria->compare('created_on',$this->created_on,true);

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
