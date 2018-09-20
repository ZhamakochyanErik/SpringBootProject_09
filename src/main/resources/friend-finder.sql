create database friend_finder character set utf8 collate utf8_general_ci;

use friend_finder;

create table city(
  id int not null auto_increment primary key ,
  name_en varchar(255) not null ,
  name_ru varchar(255) not null,
  name_arm varchar(255) not null
)engine InnoDB character set utf8 collate utf8_general_ci;

create table user(
  id int not null auto_increment primary key ,
  name varchar(255) not null ,
  surname varchar(255) not null ,
  email varchar(255) not null ,
  password varchar(255) not null ,
  age int not null ,
  birth_date date not null ,
  profile_img varchar(255),
  cover_img varchar(255),
  role varchar(255) not null ,
  token varchar(255) not null ,
  activation_type varchar(255) not null ,
  city_id int not null ,
  foreign key (city_id) references city(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table user_image(
  id int not null auto_increment primary key ,
  img_url varchar(255) not null ,
  user_id int not null ,
  foreign key (user_id) references user(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table friend_request(
  id int not null auto_increment primary key ,
  from_id int not null ,
  to_id int not null ,
  request_status varchar(255) not null ,
  send_date datetime not null ,
  foreign key (from_id) references user(id) on delete cascade ,
  foreign key (to_id) references user(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table message(
  id int not null auto_increment primary key ,
  from_id int not null ,
  to_id int not null ,
  message text,
  img_url varchar(255),
  message_status varchar(255) not null ,
  send_date datetime not null ,
  foreign key (from_id) references user(id) on delete cascade ,
  foreign key (to_id) references user(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table post(
  id int not null auto_increment primary key ,
  title varchar(255) ,
  description text,
  created_date datetime not null ,
  img_url varchar(255),
  user_id int not null ,
  foreign key (user_id) references user(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table post_likes(
  id int not null auto_increment primary key ,
  post_id int not null ,
  user_id int not null ,
  like_status varchar(255) not null ,
  foreign key (post_id) references post(id) on delete cascade ,
  foreign key (user_id) references user(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;


create table post_dislikes(
  id int not null auto_increment primary key ,
  post_id int not null ,
  user_id int not null ,
  dislike_status varchar(255) not null ,
  foreign key (post_id) references post(id) on delete cascade ,
  foreign key (user_id) references user(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table post_viewed(
  id int not null auto_increment primary key ,
  user_id int not null ,
  post_id int not null ,
  viewed_date datetime not null ,
  foreign key (user_id) references user(id) on delete cascade ,
  foreign key (post_id) references post(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table comment(
  id int not null auto_increment primary key ,
  comment text not null ,
  user_id int not null ,
  post_id int not null ,
  send_date datetime not null ,
  parent_id int,
  foreign key (user_id) references user(id) on delete cascade ,
  foreign key (post_id) references post(id) on delete cascade ,
  foreign key (parent_id) references comment(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table notification(
  id int not null auto_increment primary key ,
  notification_type varchar(255) not null ,
  notification_status varchar(255) not null ,
  notification_date datetime ,
  to_id int not null ,
  from_id int,
  post_id int,
  foreign key (to_id) references user(id) on delete cascade ,
  foreign key (from_id) references user(id) on delete cascade ,
  foreign key (post_id) references post(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table user_bookmark(
  from_id int not null ,
  to_id int not null ,
  foreign key (from_id) references user(id)on delete cascade ,
  foreign key (to_id) references user(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;


insert into city (name_en, name_ru, name_arm) values
  ('Erevan','Ереван','Երեվան'),
  ('Ashtarak','Аштарак','Աշտարակ'),
  ('Aparan','Апаран','Ապարան'),
  ('Talin','Талин','Թալին'),
  ('Artashat','Арташат','Արտաշատ'),
  ('Ararat','Арарат','Արարատ'),
  ('Vedi','Веди','Վեդի'),
  ('Masis','Масис','Մասիս'),
  ('Vagharshapat','Вагаршапат','Էջմիածին'),
  ('Armavir','Армавир','Արմավիր'),
  ('Mecamor','Мецамор','Մեծամոր'),
  ('Exegnadzor','Ехегнадзор','Եղեգնաձոր'),
  ('Vayq','Вайк','Վայք'),
  ('Djermuk','Джермук','Ջերմուկ'),
  ('Gyumri','Гюмри','Գյումրի');