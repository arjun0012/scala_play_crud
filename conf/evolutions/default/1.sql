# Employee schema

# --- !Ups
CREATE TABLE IF NOT EXISTS `scalatestdb`.`employee` (
`empId` INT(5) NOT NULL AUTO_INCREMENT,
`empName` VARCHAR(45) NULL DEFAULT NULL,
`empCompany` VARCHAR(45) NULL DEFAULT NULL,
`empSalary` INT(10) NULL DEFAULT NULL,
`empDept` VARCHAR(45) NULL DEFAULT NULL,
PRIMARY KEY (`empId`))
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8

# --- !Downs
drop table 'employee'