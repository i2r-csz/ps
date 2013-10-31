<?php

/**
 * This is the model class for table "post".
 *
 * The followings are the available columns in table 'post':
 * @property integer $endorser
 * @property integer $endorsee
 * @property integer $state
 * @property string $created_on
 * @property string $modified_on
 */
class Endorsement extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Post the static model class
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
		return 'endorsement';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('endorser, endorsee, state, modified_on, created_on', 'required'),
			array('endorser, endorsee, state', 'numerical', 'integerOnly'=>true),
			array('modified_on, created_on', 'safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('endorser, endorsee, state, modified_on, created_on', 'safe', 'on'=>'search'),
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
			'endorserUser'=>array(self::HAS_ONE, 'User', array('uid'=>'endorser')),
			'endorseeUser'=>array(self::HAS_ONE, 'User', array('uid'=>'endorsee')),
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'endorser' => 'Endorser',
			'endorsee' => 'Endorsee',
			'state' => 'State',
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

		$criteria->compare('endorser',$this->endorser);
		$criteria->compare('endorsee',$this->endorsee);
		$criteria->compare('state',$this->state);
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
