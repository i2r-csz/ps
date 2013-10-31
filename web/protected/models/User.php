<?php

/**
 * This is the model class for table "user".
 *
 * The followings are the available columns in table 'user':
 * @property integer $uid
 * @property string $email
 * @property integer $groupid
 * @property string $password
 * @property string $name
 * @property string $nickname
 * @property string $birthday
 * @property integer $sex
 * @property string $role
 * @property string $hp_number
 * @property double $cp
 * @property double $ep
 * @property double $ip
 * @property double $lp
 * @property double $q_k
 * @property double $bp
 * @property double $pti
 * @property double $r_k
 * @property double $p_k
 * @property integer $avatarid
 * @property integer $rrid
 * @property integer $sinaid
 * @property integer $created_on
 * @property integer $modified_on
 */
class User extends CActiveRecord
{
	const ROLE_MEMBER=1;
	const ROLE_STAFF=2;
	const ROLE_ADMIN=3;

	const SEX_MALE=1;
	const SEX_FEMALE=2;

	const AVATAR_FOLDER="/images/avatar/";

	public $oldpassword;
	public $password2;

	public $image;

	// New SEW calculations
	/*
	public $q_k;
	public $bp;
	public $pti;
	public $r_k;
	public $p_k;
	*/

	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return User the static model class
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
		return 'user';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('email, password, password2, name, created_on, modified_on', 'required', 'on'=>'register'),
			array('email, password, name, created_on, modified_on', 'required', 'on'=>'mobile'),
			array('email, name, nickname, birthday, sex, hp_number, created_on, modified_on', 'required', 'on'=>'update'),
			array('oldpassword, password, password2', 'required', 'on'=>'password'),
			array('email', 'required', 'on'=>'find'),


			array('groupid, sex, avatarid, rrid, sinaid', 'numerical', 'integerOnly'=>true),

			array('email, name, role', 'length', 'max'=>32),
			array('email','email'),
			array('email', 'unique', 'on'=>'register,update,password,mobile'),

			array('cp, ep, ip, lp, q_k, bp, pti, r_k, p_k', 'numerical'),

			array('oldpassword, password, password2','length','max'=>15, 'min'=>6, 'on'=>'register, password'),
			//array('password2', 'compare', 'compareAttribute'=>'password', 'on'=>'register,password, mobile'), 

			array('nickname', 'length', 'max'=>15),
			array('hp_number', 'length', 'max'=>32),
			array('birthday', 'safe'),

			array('avatarid, image', 'safe'),

			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('uid, email, groupid, password, name, nickname, birthday, sex, role, hp_number, cp, ep, ip, lp, q_k, bp, pti, r_k, p_k, avatarid, rrid, sinaid, created_on, modified_on', 'safe', 'on'=>'search'),
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
			'avatar'=>array(self::BELONGS_TO, 'Avatar', 'avatarid'),
			'avatars'=>array(self::HAS_MANY, 'Avatar', 'uid'),
			'pendingFriends'=>array(self::MANY_MANY, 'User', 'friend(tuid,fuid)','condition'=>'status=\''.Friend::STATUS_REQUESTED.'\''),
			'endorserEntries'=>array(self::HAS_MANY, 'Endorsement', array('endorsee'=>'uid')),
			'endorseeEntries'=>array(self::HAS_MANY, 'Endorsement', array('endorser'=>'uid')),
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'uid' => Yii::t('translation','Uid'),
			'email' => Yii::t('translation','Email'),
			'groupid' => 'Groupid',
			'oldpassword' => Yii::t('translation','Old Password'),
			'password' => Yii::t('translation','Password'),
			'password2'=>Yii::t('translation','Confirm Password'),
			'name' => Yii::t('translation','Name'),
			'nickname' => Yii::t('translation','Nickname'),
			'birthday' => Yii::t('translation','Birthday'),
			'sex' => 'Gender',
			'role' => 'Role',
			'hp_number' => Yii::t('translation','Mobile Number'),
			'cp' => 'Cp',
			'ep' => 'Ep',
			'ip' => 'Ip',
			'lp' => 'Lp',
			'q_k' => 'Q K',
			'bp' => 'Bp',
			'pti' => 'Pti',
			'r_k' => 'R K',
			'p_k' => 'P K',
			'avatarid' => Yii::t('translation','Avatar'),
			'rrid' => 'Rrid',
			'sinaid' => 'Sinaid',
			'created_on' => Yii::t('translation','Created On'),
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

		$criteria->compare('uid',$this->uid);
		$criteria->compare('email',$this->email,true);
		$criteria->compare('groupid',$this->groupid);
		$criteria->compare('password',$this->password,true);
		$criteria->compare('name',$this->name,true);
		$criteria->compare('nickname',$this->nickname,true);
		$criteria->compare('birthday',$this->birthday,true);
		$criteria->compare('sex',$this->sex);
		$criteria->compare('role',$this->role,true);
		$criteria->compare('hp_number',$this->hp_number,true);
		$criteria->compare('cp',$this->cp);
		$criteria->compare('ep',$this->ep);
		$criteria->compare('ip',$this->ip);
		$criteria->compare('lp',$this->lp);
		$criteria->compare('q_k',$this->q_k);
		$criteria->compare('bp',$this->bp);
		$criteria->compare('pti',$this->pti);
		$criteria->compare('r_k',$this->r_k);
		$criteria->compare('p_k',$this->p_k);
		$criteria->compare('avatarid',$this->avatarid);
		$criteria->compare('rrid',$this->rrid);
		$criteria->compare('sinaid',$this->sinaid);
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

	/**
	 ** This method will validate the password.
	 **/
	public function validatePassword($password){
		return $this->password===$this->hashPassword($password);
	}

	/**
	 * * This method will hash the password using md5.
	 * */
	public function hashPassword($password){
		$pass=md5($password);
		return $pass;
	}

	public function age(){
		if(!empty($this->birthday)){
			list($Y,$m,$d) = explode("-",$this->birthday);	
			return( date("md") < $m.$d ? date("Y")-$Y-1 : date("Y")-$Y );
		}
	}

	public function getFriends(){
		$uid=$this->uid;

		$c = new CDbCriteria();
		$c->addCondition('t.fuid=:uid OR t.tuid=:uid');
		$c->addCondition('status=:status');
		$c->order = 't.created_on DESC';
		$c->params = array(
			':uid' => $uid,
			':status'=>Friend::STATUS_CONNECTED,
		);
		$friendships=Friend::model()->findAll($c);
		$friends=array();
		foreach($friendships as $fs){
			if($fs->fuid==$uid){
				$friends[]=$fs->tuser;
			}else{
				$friends[]=$fs->fuser;
			}
		}
		return $friends;
	}

	public function getSuggestMethod(){
		$c = new CDbCriteria();
		$c->addSearchCondition('nickname', $q, true, 'OR');
		$c->addSearchCondition('name', $q, true, 'OR');
		$c->addSearchCondition('email', $q, true, 'OR');
		return $this->findAll($c);
	}
}
