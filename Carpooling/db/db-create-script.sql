create schema if not exists carpooling;


create table countries
(
    country_id int auto_increment
        primary key,
    name       varchar(50) not null
);

create table cities
(
    city_id    int auto_increment
        primary key,
    name       varchar(90) not null,
    country_id int         null,
    constraint cities_countries_country_id_fk
        foreign key (country_id) references countries (country_id)
);

create table passengers_statuses
(
    status_id int auto_increment
        primary key,
    status    varchar(200) null,
    constraint check_status_length
        check (`status` in ('Pending', 'Approved', 'Rejected'))
);

create table status_trips
(
    status_id int auto_increment
        primary key,
    status    varchar(20) null,
    constraint check_status
        check (`status` in ('In Progress', 'Cancelled', 'Upcoming', 'Completed'))
);

create table streets
(
    street_id   int auto_increment
        primary key,
    street_name varchar(100) null,
    city_id     int          not null,
    constraint streets_cities_city_id_fk
        foreign key (city_id) references cities (city_id)
);

create table tags
(
    tag_id  int auto_increment
        primary key,
    content varchar(800) not null
);

create table users_profile_pics
(
    pic_id int auto_increment
        primary key,
    pic    varchar(500) not null
);

create table users
(
    user_id      int auto_increment
        primary key,
    username     varchar(20)          not null,
    password     char(30)             not null,
    first_name   char(20)             not null,
    last_name    char(20)             not null,
    email        varchar(30)          not null,
    is_admin     tinyint(1) default 0 not null,
    is_blocked   tinyint(1) default 0 not null,
    is_enabled   tinyint(1) default 0 not null,
    profile_pic  int                  null,
    phone_number varchar(10)          not null,
    constraint users_pk
        unique (phone_number),
    constraint users_pk2
        unique (username),
    constraint users_pk3
        unique (email),
    constraint users_users_profile_pics_pic_id_fk
        foreign key (profile_pic) references users_profile_pics (pic_id)
);

create table feedbacks
(
    feedback_id  int auto_increment
        primary key,
    from_user_id int  not null,
    rating       int  not null,
    to_user_id   int  not null,
    created_at   date not null,
    constraint feedbacks_users_user_id_fk
        foreign key (from_user_id) references users (user_id),
    constraint feedbacks_users_user_id_fk2
        foreign key (to_user_id) references users (user_id),
    constraint rating_value
        check (`rating` between 1 and 5)
);

create table feedbacks_comments
(
    comment_id  int auto_increment
        primary key,
    feedback_id int           not null,
    content     varchar(8192) not null,
    user_id     int           not null,
    constraint feedbacks_comments_feedbacks_feedback_id_fk
        foreign key (feedback_id) references feedbacks (feedback_id),
    constraint feedbacks_comments_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table trips
(
    trip_id          int auto_increment
        primary key,
    driver_id        int                                   not null,
    start_point      int                                   not null,
    end_point        int                                   not null,
    departure_time   timestamp default current_timestamp() not null on update current_timestamp(),
    distance         decimal(10, 2)                        not null,
    passengers_count int                                   not null,
    status_id        int       default 2                   not null,
    created_at       timestamp default current_timestamp() not null,
    constraint trips_status_trips_status_id_fk
        foreign key (status_id) references status_trips (status_id),
    constraint trips_streets_street_id_fk
        foreign key (start_point) references streets (street_id),
    constraint trips_streets_street_id_fk2
        foreign key (end_point) references streets (street_id),
    constraint trips_users_user_id_fk
        foreign key (driver_id) references users (user_id),
    constraint check_count
        check (`passengers_count` >= 1)
);

create table passengers
(
    passenger_id int not null,
    trip_id      int not null,
    status_id    int not null,
    constraint passengers_passengers_statuses_status_id_fk
        foreign key (status_id) references passengers_statuses (status_id),
    constraint passengers_trips_trip_id_fk
        foreign key (trip_id) references trips (trip_id),
    constraint passengers_users_user_id_fk
        foreign key (passenger_id) references users (user_id)
);

create table trips_tags
(
    trip_id int not null,
    tag_id  int not null,
    constraint trips_tags_tags_tag_id_fk
        foreign key (tag_id) references tags (tag_id),
    constraint trips_tags_trips_trip_id_fk
        foreign key (trip_id) references trips (trip_id)
);

