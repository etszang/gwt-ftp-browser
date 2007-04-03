<?


require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/includes/dbUtil.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/db/UserDB.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/bo/FTPFileGroup.php5');

header("Content-type: text/plain");
session_start();

//
// REMOVE THIS LINE WHEN OUT OF DEV
//
if (!isset($_SESSION['ccuserid'])) { $_SESSION['ccuserid'] = 7; }

$ftpFileGroup = new FTPFileGroup();

$ftpFileGroup->setGroupId($_REQUEST["group_id"]);
$ftpFileGroup->setUserId($_SESSION["ccuserid"]);
$ftpFileGroup->setName($_REQUEST["name"]);

if (!$_SESSION['ccuserid']) {
	echo getJSONError(array(1, "Not logged in"));
}
else if (strlen(trim($ftpFileGroup->getName())) == 0) {
	echo getJSONError(array(11, "Name required for FTP file group"));
}
else {
	if (FTPFileGroupDB::save($ftpFileGroup)) {
		echo getJSONResult(array("ftpFileGroup" => $ftpFileGroup->toHashMap()));
	}
	else {
		echo getJSONError(array(2, "Error saving record"));
	}
}

function getJSONResult($result) {
	return json_encode(array("result" => $result));
}

function getJSONError($errors) {
	return json_encode(array("errors" => $errors));
}


?>