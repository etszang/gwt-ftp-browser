<?


require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/includes/dbUtil.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/db/UserDB.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/bo/FTPPath.php5');
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
	$arGroups = FTPFileGroupDB::retrieveForUserId($_SESSION["ccuserid"]);
	if ($arGroups != null) {
		$groupId = $arGroups[0]->getGroupId();
	}
	
	if (!groupId) {
		echo getJSONError(array(3, "Group Id required"));
	}
	else {
		$arPathRecords = FTPPathDB::retrieveForGroupId($groupId);
		$arOut = array();
		
		if ($arPathRecords != null) {
			foreach ($arPathRecords as $pathRecord) {
				$paths = explode(chr(13), $pathRecord->getPath());
				foreach ($paths as $path) {
					$parts = explode(":", $path, 2);
					$name = preg_replace('/^.+\\/([^\\/]+)$/', '$1', $parts[1]);
					array_push($arOut, array(
						 "ftp_site_id" => $pathRecord->getFtpSiteId()
						,"name" => $name
						,"type" => $parts[0]
						,"path" => $parts[1]
					));
				}
			}
			
			echo getJSONResult(array("pathList" => $arOut));
		}
		else {
			echo getJSONResult(array("pathList" => null));
		}
	}
}

function getJSONResult($result) {
	return json_encode(array("result" => $result));
}

function getJSONError($errors) {
	return json_encode(array("errors" => $errors));
}


?>