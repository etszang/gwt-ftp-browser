<?


require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/includes/dbUtil.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/db/UserDB.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/bo/FTPSite.php5');

header("Content-type: text/plain");
session_start();

//
// REMOVE THIS LINE WHEN OUT OF DEV
//
if (!isset($_SESSION['ccuserid'])) { $_SESSION['ccuserid'] = 7; }

$ftpSite = new FTPSite();

$ftpSite->setUserId($_SESSION["ccuserid"]);
$ftpSite->setFtpSiteId($_REQUEST["ftpSiteId"]);
$ftpSite->setServer($_REQUEST["server"]);
$ftpSite->setPort($_REQUEST["port"]);
$ftpSite->setUsername($_REQUEST["username"]);
$ftpSite->setPassword($_REQUEST["password"]);

if (!$_SESSION['ccuserid']) {
	echo getJSONError(array(1, "Not logged in"));
}
else if (strlen(trim($ftpSite->getServer())) == 0) {
	echo getJSONError(array(11, "Host required for FTP site"));
}
else {
	if (FTPSiteDB::save($ftpSite)) {
		echo getJSONResult(array("ftpSite" => $ftpSite->toArray()));
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