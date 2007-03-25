<?


require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/includes/dbUtil.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/db/UserDB.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/bo/FTPSite.php5');

header("Content-type: text/plain");
session_start();

//
// REMOVE THIS LINE WHEN OUT OF DEV
//
$_SESSION['ccuserid'] = 7;



if (!$_SESSION['ccuserid']) {
	echo getJSONError(array(1, "Not logged in"));
}
else {
	$rsConnections = FTPSiteDB::retrieveForUserId($_SESSION['ccuserid']);
	$arConnections = array();
	
	for ($i = 0; $i < sizeof($rsConnections); $i++) {
		array_push($arConnections, $rsConnections[$i]->toHasMap());
	}
	
	echo getJSONResult(array("connectionList" => $arConnections));
}

function getJSONResult($result) {
	return json_encode(array("result" => $result));
}

function getJSONError($errors) {
	return json_encode(array("errors" => $errors));
}


?>