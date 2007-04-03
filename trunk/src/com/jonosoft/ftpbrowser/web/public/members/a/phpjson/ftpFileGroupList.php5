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

if (!$_SESSION['ccuserid']) {
	echo getJSONError(array(1, "Not logged in"));
}
else {
	
	$rsPaths = FTPFileGroupDB::retrieveForUserId($_SESSION['ccuserid']);
	$arPaths = array();
	for ($i = 0; $i < sizeof($rsPaths); $i++) {
		array_push($arPaths, $rsPaths[$i]->toHashMap());
	}
	
	echo getJSONResult(array("groupList" => $arPaths));
}

function getJSONResult($result) {
	return json_encode(array("result" => $result));
}

function getJSONError($errors) {
	return json_encode(array("errors" => $errors));
}


?>