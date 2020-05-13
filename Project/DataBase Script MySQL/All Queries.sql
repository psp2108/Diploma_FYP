drop database onlineattendence;
create database onlineattendence;
use onlineattendence;

create table Location_Master(
	Location_ID bigint Primary Key AUTO_INCREMENT,
	Location_Name Varchar(1024) unique
);

create table Department_Master(
	Dept_ID bigint Primary Key AUTO_INCREMENT,
	Dept_Name Varchar(500),
	Dept_Description Varchar(1000),
	Dept_Location bigint references Location_Master(Location_ID)
);

create table Hire_Details_Table(
	Hire_ID bigint Primary Key AUTO_INCREMENT,
	Hire_fromDate Date,
	Hire_toDate Date
);

create table Employee_Master(
	Emp_ID bigint Primary Key AUTO_INCREMENT,
	Emp_PSNumber varchar(400) unique,
	Emp_FirstName Varchar(200),
	Emp_LastName Varchar(200),
	Emp_Dept bigint references Department_Master(Dept_ID),
	Emp_Email Varchar(500) unique,
	Emp_DOB Date,
	Emp_Gender Varchar(6),
	Emp_HireID bigint references Hire_Details_Table(Hire_ID)
);

create Table Attendence_Type_Table(
	AttendenceType_ID  bigint Primary Key AUTO_INCREMENT,
	Attendence_Type Varchar(50) unique
);

create table Attendence_Master(
	Attendence_ID bigint Primary Key AUTO_INCREMENT,
	Attendence_Date date,
	Attendence_EmpID bigint references Employee_Master(Emp_ID),
	Attendence_InTimeStamp Time,
	Attendence_OutTimeStamp Time,
	Attendence_DurationInMinutes bigint,
	Attendence_Stamp bigint references Attendence_Type_Table(AttendenceType_ID)
);

create table Fingerprint_Master(
	FP_ID bigint Primary Key AUTO_INCREMENT,
	FP_Type Varchar(100),
	FP_Image Varchar(500), 
	FP_EmpID bigint references Employee_Master(Emp_ID), 
	FP_SensorID int, 
	FP_DeptID bigint references Department_Master(Dept_ID)
);

create table Permission_Level(
	Perm_ID bigint Primary Key AUTO_INCREMENT,
	Perm_Type Varchar(200) unique
);

create table Admin_Login(
	Admin_ID bigint Primary Key AUTO_INCREMENT,
	Admin_FirstName Varchar(200),
	Admin_LastName Varchar(200),
	Admin_UID Varchar(20) unique,
	Admin_PWD Varchar(200) unique,
	Admin_Email Varchar(500),
	Admin_Dept bigint references Department_Master(Dept_ID),
	Admin_DOB Date,
	Admin_Gender  Varchar(6),
	Admin_Permission bigint references Permission_Level(Perm_ID)
);

create table Registration_Table(
	Registration_ID bigint Primary Key AUTO_INCREMENT,
	Registration_Admin bigint references Admin_Login(Admin_ID),
	Registration_Employee bigint references Employee_Master(Emp_ID),
	Registration_Date date,
	Registration_Time time
);

create Table Holiday_Master(
	Holiday_ID bigint primary key AUTO_INCREMENT,
    Holiday_For varchar(20),
    Holiday_Date date
);

insert into Attendence_Type_Table(Attendence_Type) values ('P');
insert into Attendence_Type_Table(Attendence_Type) values ('A');
insert into Attendence_Type_Table(Attendence_Type) values ('L');
insert into Attendence_Type_Table(Attendence_Type) values ('H');

insert into Permission_Level (Perm_Type) values ("Main Admin");
insert into Permission_Level (Perm_Type) values ("Sub Admin");

#1. Enroll and get new ID (Employee)
drop procedure if exists insertEmployeeDetails;

DELIMITER //
Create procedure insertEmployeeDetails(
	in Emp_PSNo varchar(400),
	in FirstName varchar(200),
	in LastName varchar(200),
	in DepartmentID bigint,
	in Email varchar(500),
	in DOB date,
	in Gender varchar(6),
	in HireFrom date,
	in HireTill date,
	in RegistrationTime datetime,
	in AdminUID varchar(20)
)
begin 
	Declare AdminID bigint;
	Declare EmployeeID bigint;
	Declare HireID bigint;
	Declare EmptyFingerprint boolean;
	Declare minFingerprintID int;
	Declare maxFingerprintID int;
	
	set EmptyFingerprint = false;
	set minFingerprintID = 1;
	set maxFingerprintID = 162;
	
	looptogetid: while minFingerprintID <= maxFingerprintID
	do
		if not exists (select * from Fingerprint_Master where FP_SensorID = minFingerprintID and FP_DeptID = DepartmentID) then
			set EmptyFingerprint = true;
			leave looptogetid;
		end if;
		set minFingerprintID = minFingerprintID + 1;
	end while;
	
	if EmptyFingerprint then
		insert into Hire_Details_Table (Hire_fromDate, Hire_toDate) values (HireFrom, HireTill);
		
		set HireID = (select Hire_ID from Hire_Details_Table order by Hire_ID desc limit 1);
		
		insert into Employee_Master (Emp_PSNumber, Emp_FirstName, Emp_LastName, Emp_Dept, Emp_Email, Emp_DOB, Emp_Gender, Emp_HireID) 
		values (Emp_PSNo, FirstName, LastName, DepartmentID, Email, DOB, Gender, HireID);
		
		set EmployeeID = (select Emp_ID from Employee_Master order by Emp_ID desc limit 1);
		
		set AdminID = (select Admin_ID from Admin_Login where Admin_UID = AdminUID);
		
		insert into Registration_Table (Registration_Admin, Registration_Employee, Registration_Date, Registration_Time) 
		values (AdminID, EmployeeID, RegistrationTime, RegistrationTime);
		
		insert into Fingerprint_Master (FP_EmpID,FP_SensorID,FP_DeptID) values (EmployeeID,minFingerprintID,DepartmentID);
		
		select minFingerprintID;
	else
		select "Sensor limit reached. Record not inserted!";
	end if;
	
End //
DELIMITER ;

#2. Mark attendance based on ID
drop procedure if exists markAttendence;

DELIMITER //
Create procedure markAttendence(
	in CurrentDate datetime,
	in FingerPrintID bigint,
	in AdminUserID varchar(20) 
)
begin
	Declare Emp_ID bigint;
	Declare Attendence_Type tinyint;
	Declare DepartmentID bigint;
	
	set DepartmentID = (select Admin_Dept from Admin_Login where Admin_UID = AdminUserID);
	set Attendence_Type = 1;
	set Emp_ID = (select FP_EmpID from Fingerprint_Master where FP_SensorID = FingerPrintID and FP_DeptID = DepartmentID);
	
    if exists (select Attendence_InTimeStamp 
		from Attendence_Master 
		where Attendence_Date = Date(CurrentDate) 
		and Attendence_EmpID = Emp_ID) then
	
		update Attendence_Master set Attendence_OutTimeStamp = CurrentDate
        where Attendence_Date = Date(CurrentDate) 
		and Attendence_EmpID = Emp_ID;
    
    else
    
		insert into Attendence_Master (Attendence_Date,Attendence_EmpID,Attendence_InTimeStamp,Attendence_Stamp) 
		values 
		(CurrentDate,Emp_ID,CurrentDate,Attendence_Type);
        
    end if;
	
	select "Record Inserted";
End //
DELIMITER ;

#3. Get Employee name and Ps number based on ID
drop procedure if exists getEmpNameAndPS;

DELIMITER //
Create Procedure getEmpNameAndPS(
	in ID bigint,
	in AdminUserID varchar(20) 
)
begin
	Declare DepartmentID bigint;
	
	set DepartmentID = (select Admin_Dept from Admin_Login where Admin_UID = AdminUserID);

	select Emp_PSNumber, Emp_FirstName, Emp_LastName 
	from Employee_Master, Fingerprint_Master, Department_Master where
	Fingerprint_Master.FP_EmpID = Employee_Master.Emp_ID and
    Fingerprint_Master.FP_DeptID = Department_Master.Dept_ID and
	Fingerprint_Master.FP_SensorID = ID and 
	Fingerprint_Master.FP_DeptID = DepartmentID;
End //
DELIMITER ;

#4. Verify Admin username and password
drop procedure if exists getHashedPassword;

DELIMITER //
Create Procedure getHashedPassword(
	in UserID varchar(20)
)
begin
	select Admin_PWD, Admin_Permission from Admin_Login where Admin_UID = UserID;
End //
DELIMITER ;

#5. Register new admin login details
drop procedure if exists insertAdminCredentials;


DELIMITER //
Create Procedure insertAdminCredentials(
	in UserID varchar(20),
	in Password varchar(200),
	in PasswordHash2 varchar(200)
)
begin
	declare ifUIDExists int;
	
	set ifUIDExists = (select count(*) from Admin_Login where Admin_UID = UserID);
	
	if (ifUIDExists = 0) then
		insert into Admin_Login (Admin_UID,Admin_PWD) values (UserID,Password);
		select "Record Inserted";
	else
		select "UserID taken";
	end if;
	
End //
DELIMITER ;

#6 Register new admin other details with new department
drop procedure if exists updateAdminProfileNewDepartment;

DELIMITER //
Create Procedure updateAdminProfileNewDepartment(
	in FirstName varchar(200),
	in LastName varchar(200),
	in UserID varchar(20),
	in Email varchar(500),
	in DOB date,
	in Gender varchar(6),
	in DepartmentName varchar(500),
	in DepartmentDescription varchar(1000),
	in DepartmentLocation varchar(1024)
)
begin
	Declare DepartmentID bigint;
	Declare LocationID bigint;
	Declare AdminRecords bigint;
	Declare AdminPermission tinyint;
	
	set AdminPermission = (Select Admin_Permission from Admin_Login where Admin_UID = UserID);
	set AdminRecords = (select count(Admin_Permission) from Admin_Login);
	
	if AdminRecords = 0 then
		set AdminPermission = 1;
	else
		if ((AdminPermission != 1) and (AdminPermission != 2)) or ((AdminPermission is null)) then
			set AdminPermission = 2;
		end if;
	end if;
	
	
	set LocationID = (select count(*) from Location_Master where Location_Name = DepartmentLocation);
	
	if (LocationID = 0) then
		insert into Location_Master 
		(Location_Name) values 
		(DepartmentLocation);

		set LocationID = (select Location_ID from Location_Master order by Location_ID desc limit 1);
	else
		set LocationID = (select Location_ID from Location_Master where Location_Name = DepartmentLocation);
	end if;
	
	if not exists (select * from Department_Master where Dept_Name = DepartmentName and Dept_Description = DepartmentDescription and Dept_Location = LocationID) then
		insert into Department_Master (Dept_Name, Dept_Description, Dept_Location) values
		(DepartmentName, DepartmentDescription, LocationID);
		
		set DepartmentID = (select Dept_ID from Department_Master order by Dept_ID desc limit 1);
	else	
		set DepartmentID = (select Dept_ID from Department_Master where Dept_Name = DepartmentName and Dept_Description = DepartmentDescription and Dept_Location = LocationID);
	end if;

	update Admin_Login set 
	Admin_FirstName = FirstName,
	Admin_LastName = LastName,
	Admin_Email = Email,
	Admin_DOB = DOB,
	Admin_Gender = Gender,
	Admin_Dept = DepartmentID,
	Admin_Permission = AdminPermission 
	where
	Admin_UID = UserID;
	
	select "Record Inserted";
	
End //
DELIMITER ;

#7 Register new admin other details with existed department
drop procedure if exists updateAdminProfileOldDepartment;

DELIMITER //
Create Procedure updateAdminProfileOldDepartment(
	in FirstName varchar(200),
	in LastName varchar(200),
	in UserID varchar(20),
	in Email varchar(500),
	in DOB date,
	in Gender varchar(6),
	in DepartmentID bigint 
)
begin
	Declare AdminRecords bigint;
	Declare AdminPermission tinyint;
		
	set AdminPermission = (Select Admin_Permission from Admin_Login where Admin_UID = UserID);
	set AdminRecords = (select count(Admin_Permission) from Admin_Login);
	
	if AdminRecords = 0 then
		set AdminPermission = 1;
	else
		if ((AdminPermission != 1) and (AdminPermission != 2)) or ((AdminPermission is null)) then
			set AdminPermission = 2;
		end if;
	end if;
	
	update Admin_Login set 
	Admin_FirstName = FirstName,
	Admin_LastName = LastName,
	Admin_Email = Email,
	Admin_DOB = DOB,
	Admin_Gender = Gender,
	Admin_Dept = DepartmentID,
	Admin_Permission = AdminPermission
	where
	Admin_UID = UserID;
	
	select "Record Inserted";
End //
DELIMITER ;

#8. Display Departments
drop procedure if exists getDepartmentList;

DELIMITER //
Create Procedure getDepartmentList()
begin
	select Dept_ID, Dept_Name, Dept_Description, Location_Name from 
	Department_Master, Location_Master where
	Dept_Location = Location_ID;

End //
DELIMITER ;

#9. Display Locations
drop procedure if exists getLocationList;

DELIMITER //
Create Procedure getLocationList()
begin
	select Location_ID, Location_Name from 
	Location_Master order by Location_Name;
	
End //
DELIMITER ;

#10. Retrive Email ID of Admin
drop procedure if exists getAdminEmail;

DELIMITER //
Create Procedure getAdminEmail(
	in UserID varchar(20)
)
begin
	
	Select Admin_Email from Admin_Login where Admin_UID = UserID;
	
End //
DELIMITER ;

#11. Update Password of Admin
drop procedure if exists updateAdminPassword;

DELIMITER //
Create Procedure updateAdminPassword(
	in UserID varchar(20),
	in Password varchar(200),
	in PasswordHash2 varchar(200)
)
begin
	
	update Admin_Login 
	set Admin_PWD = Password
	where Admin_UID = UserID;
	
	Select "Password Updated";
	
End //
DELIMITER ;

#12. Get Profile Details of Admin
drop procedure if exists getAdminProfile;

DELIMITER //
Create Procedure getAdminProfile(
	in UserID varchar(20)
)
begin
	
	Select 
	Admin_FirstName,
	Admin_LastName,
	Admin_Email,
	Admin_DOB,
	Admin_Gender,
	Dept_Name,
	Dept_Description,
	Location_Name
	from 
	Admin_Login, Department_Master, Location_Master
	where 
	Admin_Dept = Dept_ID and
	Dept_Location = Location_ID and
	Admin_UID = UserID;
	
End //
DELIMITER ;

#13. Retrives the list of all employees
drop procedure if exists getEmployeeList;

DELIMITER //
Create Procedure getEmployeeList(
	in UserID varchar(20)
)
begin
	
	Declare DepartmentID bigint;
        
    if(UserID is null) 
    then
		
        Select 
		Emp_ID, Emp_PSNumber, Emp_FirstName, Emp_LastName, FP_SensorID
		from 
		Employee_Master, Fingerprint_Master
		where
		FP_EmpID = Emp_ID;
    
    else 
    
		set DepartmentID = (select Admin_Dept from Admin_Login where Admin_UID = UserID);
		
		Select 
		Emp_ID, Emp_PSNumber, Emp_FirstName, Emp_LastName, FP_SensorID
		from 
		Employee_Master, Fingerprint_Master
		where
		FP_EmpID = Emp_ID and
		Emp_Dept = DepartmentID;
    
    end if;
    
	
End //
DELIMITER ;

#14. Check if the Admin Username is available or not
drop procedure if exists checkAdminUserAvailability;

DELIMITER //
Create Procedure checkAdminUserAvailability(
	in NewUserID varchar(20)
)
begin
	
	Declare Available tinyint;
	
	set Available = (Select count(*) from Admin_Login where Admin_UID = NewUserID);
	
	if Available = 0 then
		Select "true";
	else
		Select "false";
	end if;
	
End //
DELIMITER ;


#15. Remove Employee Biometrics
drop procedure if exists removeEmployeeBiometrics;

DELIMITER //
Create Procedure removeEmployeeBiometrics(
	in EmployeeSensorFPID bigint,
	in UserID varchar(20)
)
begin

	Declare DepartmentID bigint;
	
	set DepartmentID = (select Admin_Dept from Admin_Login where Admin_UID = UserID);
	
	Delete from Fingerprint_Master where FP_SensorID = EmployeeSensorFPID and FP_DeptID = DepartmentID;
	Select "Deleted Successfully";
	
End //
DELIMITER ;


#16. Check if Admin_Login table is empty or not and returns true if empty else false
drop procedure if exists checkIfAnyAdminIsAvailable;

DELIMITER //
Create Procedure checkIfAnyAdminIsAvailable()
begin
	
	Declare Available tinyint;
	
	set Available = (Select count(*) from Admin_Login);
	
	if Available = 0 then
		Select "true";
	else
		Select "false";
	end if;
	
End //
DELIMITER ;

#17. Procedure to get list of employee specific attendence
drop procedure if exists displayEmployeeAttendence;

DELIMITER //
Create Procedure displayEmployeeAttendence(
	IN EmployeeID bigint,
    IN StartDate Date,
    IN EndDate Date
)
begin

	Declare MinDate date;
	Declare MaxDate date;
	Declare EmpHireDate date;
	Declare IfHoliday int;

	if (StartDate is null)
    then
		set MinDate = (select min(Attendence_Date) from Attendence_Master);
    else
		set MinDate = StartDate;
    end if;

	if (EndDate is null)
    then
		set MaxDate = (select max(Attendence_Date) from Attendence_Master);
    else
		set MaxDate = EndDate;
    end if;

	set EmpHireDate = (select Hire_fromDate from Hire_Details_Table, Employee_Master where Emp_HireID = Hire_ID and Emp_ID = EmployeeID);

	if(MinDate < EmpHireDate) then
		set MinDate = EmpHireDate;
	end if;
    
	call setSaturdatAndSunday(MinDate);
	
    if(MaxDate > date(now())) then
		set MaxDate = date(now());
	end if;

	while MinDate != DATE_ADD(MaxDate, INTERVAL 1 DAY)
	do
    
		if not exists (select * from Attendence_Master where Attendence_Date = MinDate and Attendence_EmpID = EmployeeID)
        then
        
			set IfHoliday = (select count(*) from Holiday_Master where Holiday_Date = MinDate);
            
            if (IfHoliday >= 1) then
				insert into Attendence_Master (Attendence_Date, Attendence_EmpID, Attendence_Stamp) 
				values (MinDate, EmployeeID, 4);
            else
				insert into Attendence_Master (Attendence_Date, Attendence_EmpID, Attendence_Stamp) 
				values (MinDate, EmployeeID, 2);
            end if;
        
        end if;
    
		set MinDate = DATE_ADD(MinDate, INTERVAL 1 DAY);
	end while; 

	if((StartDate is null) && (EndDate is null))
    then
    
		select 
        Emp_PSNumber,
        Emp_FirstName,
        Emp_LastName,
		Attendence_Date,
		Attendence_InTimeStamp,
		Attendence_OutTimeStamp,
		Attendence_Type
		from Attendence_Master, Attendence_Type_Table, Employee_Master
		where
		AttendenceType_ID = Attendence_Stamp and
        Attendence_EmpID = Emp_ID and
		Attendence_EmpID = EmployeeID
		order by Attendence_Date Desc;
    
    else
    
		if (StartDate is null)
		then
        
			select 
			Emp_PSNumber,
			Emp_FirstName,
			Emp_LastName,
			Attendence_Date,
			Attendence_InTimeStamp,
			Attendence_OutTimeStamp,
			Attendence_Type
			from Attendence_Master, Attendence_Type_Table, Employee_Master
			where
			AttendenceType_ID = Attendence_Stamp and
			Attendence_EmpID = Emp_ID and
			Attendence_EmpID = EmployeeID and 
            Attendence_Date <= EndDate
			order by Attendence_Date Desc;
        
        elseif(EndDate is null)
        then 
        
			select 
			Emp_PSNumber,
			Emp_FirstName,
			Emp_LastName,
			Attendence_Date,
			Attendence_InTimeStamp,
			Attendence_OutTimeStamp,
			Attendence_Type
			from Attendence_Master, Attendence_Type_Table, Employee_Master
			where
			AttendenceType_ID = Attendence_Stamp and
			Attendence_EmpID = Emp_ID and
			Attendence_EmpID = EmployeeID and
            Attendence_Date >= StartDate 
			order by Attendence_Date Desc;
        
        else
        
			select 
			Emp_PSNumber,
			Emp_FirstName,
			Emp_LastName,
			Attendence_Date,
			Attendence_InTimeStamp,
			Attendence_OutTimeStamp,
			Attendence_Type
			from Attendence_Master, Attendence_Type_Table, Employee_Master
			where
			AttendenceType_ID = Attendence_Stamp and
			Attendence_EmpID = Emp_ID and
			Attendence_EmpID = EmployeeID and
            Attendence_Date >= StartDate and
            Attendence_Date <= EndDate 
			order by Attendence_Date Desc;
        
        end if;
    
    end if;

End //
DELIMITER ;

#18. Retrives the list of all employees
drop procedure if exists getEmployeeListByDepartment;

DELIMITER //
Create Procedure getEmployeeListByDepartment(
	in DepartmentID bigint
)
begin
	
    if(DepartmentID is null) 
    then
		
        Select 
		Emp_ID, Emp_PSNumber, Emp_FirstName, Emp_LastName, FP_SensorID
		from 
		Employee_Master, Fingerprint_Master
		where
		FP_EmpID = Emp_ID;
    
    else 
    
		Select 
		Emp_ID, Emp_PSNumber, Emp_FirstName, Emp_LastName, FP_SensorID
		from 
		Employee_Master, Fingerprint_Master
		where
		FP_EmpID = Emp_ID and
		Emp_Dept = DepartmentID;
    
    end if;
	
End //
DELIMITER ;

#19. Set Holiday Master TABLE
drop procedure if exists setSaturdatAndSunday;

DELIMITER //
Create Procedure setSaturdatAndSunday(
    IN StartDate Date
)
begin
	
	Declare StartingDate Date;
	Declare EndingDate Date;
	Declare TempWeek int;
	set EndingDate = date(now());
	
	if (StartDate is null)
    then
		set StartDate = (select max(Holiday_Date) from Holiday_Master);
    else
		set StartingDate = StartDate;
    end if;
	
	while StartingDate != DATE_ADD(EndingDate, INTERVAL 1 DAY)
	do
		if not exists (select * from Holiday_Master where Holiday_Date = StartingDate) 
		then 
			set TempWeek = (select date_format(date(StartingDate), '%w'));
			
			if TempWeek = 0 
			then
				insert into Holiday_Master (Holiday_For, Holiday_Date) values ('Sunday', StartingDate); 
			elseif TempWeek = 6
            then
				insert into Holiday_Master (Holiday_For, Holiday_Date) values ('Saturday', StartingDate); 			
			end if;
		end if;
		
		set StartingDate = DATE_ADD(StartingDate, INTERVAL 1 DAY);
	end while; 
	
	
End //
DELIMITER ;

call setSaturdatAndSunday('2000-01-01');

#20. Insert into Holiday Master
drop procedure if exists addHoliday;

DELIMITER //
Create Procedure addHoliday(
    IN DateAt Date,
	IN HolidayPurpose varchar(20)
)
begin
	
	call refreshAttendance();
	insert into Holiday_Master (Holiday_For, Holiday_Date) values (HolidayPurpose, DateAt); 
	select "Record Inserted";
	
End //
DELIMITER ;

#21. Refresh Attendance
drop procedure if exists refreshAttendance;

DELIMITER //
Create Procedure refreshAttendance()
begin
	
	delete from attendence_master where Attendence_Stamp != 1;
	
End //
DELIMITER ;

#22. Retrives the list of all employees for reporting
drop procedure if exists getEmployeeListByDepartmentReport;

DELIMITER //
Create Procedure getEmployeeListByDepartmentReport(
	in DepartmentID bigint
)
begin
	
    if(DepartmentID is null) 
    then
		
        Select 
		Emp_ID, Emp_PSNumber, Emp_FirstName, Emp_LastName
		from 
		Employee_Master;
    
    else 
    
		Select 
		Emp_ID, Emp_PSNumber, Emp_FirstName, Emp_LastName
		from 
		Employee_Master
		where
		Emp_Dept = DepartmentID;
    
    end if;
	
End //
DELIMITER ;

