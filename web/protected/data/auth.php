<?php
return array (
  'member' => 
  array (
    'type' => 2,
    'description' => '',
    'bizRule' => '',
    'data' => '',
    'assignments' => 
    array (
      2 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
      3 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
      4 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
      5 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
      19 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
      20 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
    ),
  ),
  'staff' => 
  array (
    'type' => 2,
    'description' => '',
    'bizRule' => '',
    'data' => '',
    'children' => 
    array (
      0 => 'member',
    ),
  ),
  'admin' => 
  array (
    'type' => 2,
    'description' => '',
    'bizRule' => '',
    'data' => '',
    'children' => 
    array (
      0 => 'staff',
    ),
    'assignments' => 
    array (
      3 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
      5 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
      1 => 
      array (
        'bizRule' => NULL,
        'data' => NULL,
      ),
    ),
  ),
);
