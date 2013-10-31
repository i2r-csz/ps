<?php

/**
 * This is the model class for table "friend".
 *
 * The followings are the available columns in table 'friend':
 * @property integer $fid
 * @property integer $fuid
 * @property integer $tuid
 * @property string $status
 * @property string $comment
 * @property string $created_on
 * @property string $modified_on
 */
class Friend extends CActiveRecord
{
	const STATUS_REQUESTED="REQUESTED";
	const STATUS_CONNECTED="CONNECTED";

	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Friend the static model class
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
		return 'friend';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('fuid, tuid, status, created_on, modified_on', 'required'),
			array('fuid, tuid', 'numerical', 'integerOnly'=>true),
			array('status', 'length', 'max'=>9),
			array('comment', 'length', 'max'=>255),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('fid, fuid, tuid, status, comment, created_on, modified_on', 'safe', 'on'=>'search'),
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
			'fuser'=>array(self::BELONGS_TO, 'User', 'fuid'),
			'tuser'=>array(self::BELONGS_TO, 'User', 'tuid'),
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'fid' => 'Fid',
			'fuid' => 'Fuid',
			'tuid' => 'Tuid',
			'status' => 'Status',
			'comment' => 'Comment',
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

		$criteria->compare('fid',$this->fid);
		$criteria->compare('fuid',$this->fuid);
		$criteria->compare('tuid',$this->tuid);
		$criteria->compare('status',$this->status,true);
		$criteria->compare('comment',$this->comment,true);
		$criteria->compare('created_on',$this->created_on,true);
		$criteria->compare('modified_on',$this->modified_on,true);

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

	/**
	 ** This method will return the relationship of two users
	 **/
	public static function findModel($fuid, $tuid){
		$condition="(fuid=:fuid AND tuid=:tuid) OR (fuid=:tuid AND tuid=:fuid)";
		$params=array(":fuid"=>$fuid, ":tuid"=>$tuid);
		$relation=self::model()->find($condition, $params);
		return $relation;
	}
}
