<?php

/**
 * This is the model class for table "post".
 *
 * The followings are the available columns in table 'post':
 * @property string $id
 * @property integer $uid
 * @property string $clusterid
 * @property integer $verdictid
 * @property string $username
 * @property string $user_cp
 * @property string $user_ip
 * @property double $lat
 * @property double $lng
 * @property double $mlat
 * @property double $mlng
 * @property double $accuracy
 * @property string $subject
 * @property string $description
 * @property integer $category
 * @property string $severity
 * @property string $vote_up
 * @property string $vote_down
 * @property string $start_date
 * @property string $end_date
 * @property string $modified_on
 * @property string $created_on
 * @property integer $complete
 * @property string $state
 * @property integer $trust
 * @property string $remarks
 */
class Post extends CActiveRecord
{

	// Custom properties
	public $maxCluster; // To support selection of max($clusterid)
	public $wz; // To assist in SEW calculations

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
		return 'post';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('uid, lat, lng, description, modified_on, created_on', 'required'),
			array('uid, complete, trust, category', 'numerical', 'integerOnly'=>true),
			array('lat, lng, mlat, mlng, accuracy', 'numerical'),
			array('clusterid, vote_up, vote_down', 'length', 'max'=>20),
			array('username', 'length', 'max'=>32),
			array('user_cp, user_ip, severity', 'length', 'max'=>10),
			array('subject, state', 'length', 'max'=>64),
			array('remarks', 'length', 'max'=>'255'),
			array('start_date, end_date, created_on', 'safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('id, uid, clusterid, verdictid, username, user_cp, user_ip, lat, lng, mlat, mlng, accuracy, subject, description, category, severity, vote_up, vote_down, start_date, endd_ate, modified_on, created_on, complete, state, trust, remarks', 'safe', 'on'=>'search'),
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
			'photo'=>array(self::HAS_ONE, 'PostPhoto', 'pid'),
			'creator'=>array(self::BELONGS_TO, 'User', 'uid'),
			'thumbs'=>array(self::HAS_MANY, 'Thumbs', 'uid')
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'id' => 'ID',
			'uid' => 'Uid',
			'clusterid' => 'Clusterid',
			'verdictid' => 'Verdictid',
			'username' => 'Username',
			'user_cp' => 'User Cp',
			'user_ip' => 'User Ip',
			'lat' => 'Lat',
			'lng' => 'Lng',
			'mlat' => 'Mlat',
			'mlng' => 'Mlng',
			'accuracy' => 'Accuracy',
			'subject' => 'Subject',
			'description' => 'Description',
			'category' => 'Category',
			'severity' => 'Severity',
			'vote_up' => 'Vote Up',
			'vote_down' => 'Vote Down',
			'start_date' => 'Start Date',
			'end_date' => 'End Date',
			'modified_on' => 'Modified On',
			'created_on' => 'Created On',
			'complete' => 'Complete',
			'state' => 'State',
			'trust' => 'Trust',
			'remarks' => 'Remarks',
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

		$criteria->compare('id',$this->id,true);
		$criteria->compare('uid',$this->uid);
		$criteria->compare('clusterid',$this->clusterid,true);
		$criteria->compare('verdictid',$this->verdictid);
		$criteria->compare('username',$this->username,true);
		$criteria->compare('user_cp',$this->user_cp,true);
		$criteria->compare('user_ip',$this->user_ip,true);
		$criteria->compare('lat',$this->lat);
		$criteria->compare('lng',$this->lng);
		$criteria->compare('mlat',$this->mlat);
		$criteria->compare('mlng',$this->mlng);
		$criteria->compare('accuracy',$this->accuracy);
		$criteria->compare('subject',$this->subject,true);
		$criteria->compare('description',$this->description,true);
		$criteria->compare('category',$this->category);
		$criteria->compare('severity',$this->severity,true);
		$criteria->compare('vote_up',$this->vote_up,true);
		$criteria->compare('vote_down',$this->vote_down,true);
		$criteria->compare('start_date',$this->start_date,true);
		$criteria->compare('end_date',$this->end_date,true);
		$criteria->compare('modified_on',$this->modified_on,true);
		$criteria->compare('created_on',$this->created_on,true);
		$criteria->compare('complete',$this->complete);
		$criteria->compare('state',$this->state,true);
		$criteria->compare('trust',$this->trust);
		$criteria->compare('remarks',$this->remarks,true);

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
