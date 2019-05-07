create table Roles
(
    Id   int auto_increment
        primary key,
    Name varchar(32) not null,
    constraint Name
        unique (Name)
);

create table Users
(
    Id         int auto_increment
        primary key,
    Email      varchar(64) not null,
    Password   varchar(64) not null,
    CreateDate datetime    not null,
    Username   varchar(64) null,
    constraint Email
        unique (Email)
);

create table Credits
(
    Id            int auto_increment
        primary key,
    Name          varchar(32)    not null,
    DesiredSum    decimal(15, 2) not null,
    MonthsCount   int            not null,
    InterestRate  double         not null,
    MonthlyCharge decimal(15, 2) null,
    TotalSum      decimal(15, 2) null,
    CreatedDate   datetime       not null,
    UserId        int            not null,
    constraint Credits_ibfk_1
        foreign key (UserId) references Users (Id)
            on update cascade on delete cascade
);

create index UserId
    on Credits (UserId);

create table RefreshTokens
(
    Id             int auto_increment
        primary key,
    Value          varchar(512) not null,
    ExpirationDate datetime     not null,
    UserId         int          not null,
    constraint RefreshTokens_ibfk_1
        foreign key (UserId) references Users (Id)
            on update cascade on delete cascade
);

create index UserId
    on RefreshTokens (UserId);

create table UsersRoles
(
    UserId int not null,
    RoleId int not null,
    constraint UserId
        unique (UserId, RoleId),
    constraint UsersRoles_ibfk_1
        foreign key (UserId) references Users (Id)
            on update cascade on delete cascade,
    constraint UsersRoles_ibfk_2
        foreign key (RoleId) references Roles (Id)
            on update cascade on delete cascade
);

create index RoleId
    on UsersRoles (RoleId);