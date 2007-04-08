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
	$groupId = $_POST["group_id"];
	unset($_POST["group_id"]);
	
	// If no groupId was provided, look one up for the current user,
	// if none no groups exist, we'll create a default one and use that;
	// otherwise, if one exists with a name of "Default Group", we'll use that;
	// otherwise, an error is thrown back since a group must be specified.
	if (!($groupId)) {
		$ar = FTPFileGroupDB::retrieveForUserId($_SESSION["ccuserid"]);
		if ($ar == null) {
			$ftpFileGroup = new FTPFileGroup();
			$ftpFileGroup->setUserId($_SESSION["ccuserid"]);
			$ftpFileGroup->setName("Default Group");
			FTPFileGroupDB::save($ftpFileGroup);
			$groupId = $ftpFileGroup->getGroupId();
		}
		else if (sizeof($ar) == 1) {
			if ($ar[0]->getName() == "Default Group") {
				$groupId = $ar[0]->getGroupId();
			}
		}
	}
	
	// same test as above, if we still don't have one, then don't proceed
	if (($groupId)) {
		$successful = false;
		$arFtpSiteIds = array();
		
		if (sizeof($_POST) == 0) {
			$cleanupRS = new dbRS();
			$cleanupRS->execute(sprintf("delete from ftp_path where group_id = %s",
				dbUtil::encode($groupId)));
			echo getJSONResult(array("ftpPath" => "saved2"));
		}
		else {
			foreach ($_POST as $ftpSiteId => $path) {
				if (is_int($ftpSiteId)) {
					array_push($arFtpSiteIds, $ftpSiteId);
					
					$ftpPath = new FTPPath();
					
					// For Base64 decoding
					$path = str_replace("_", "+", $path);
					$path = str_replace(".", "/", $path);
					$path = str_replace("-", "=", $path);
					
					//$ftpPath->setFtpPathId($_SESSION["ftpPathId"]);
					$ftpPath->setFtpSiteId($ftpSiteId);
					$ftpPath->setGroupId($groupId);
					$ftpPath->setPath(base64_decode($path));
					/*$ftpPath->setPath($path);*/
					
					$successful = FTPPathDB::save($ftpPath);
				}
			}
			if ($successful) {
				$cleanupRS = new dbRS();
				if (sizeof($arFtpSiteIds) > 0) {
					$inString = '';
					foreach ($arFtpSiteIds as $ftpSiteId) {
						if (strlen($inString) > 0) {
							$inString .= ',';
						}
						$inString .= $ftpSiteId;
					}
					$cleanupRS->execute(sprintf("delete from ftp_path where group_id = %s and ftp_site_id not in (%s)",
						 dbUtil::encode($groupId)
						,$inString));
				}
				else {
					$cleanupRS->execute(sprintf("delete from ftp_path where group_id = %s",
						dbUtil::encode($groupId)));
				}
				echo getJSONResult(array("ftpPath" => "saved"));
			}
			else {
				echo getJSONError(array(2, "Error saving FTPPath record"));
			}
		}
	}
	else {
		echo getJSONError(array(12, "Group Id must be specified when saving paths"));
	}
}

function getJSONResult($result) {
	return json_encode(array("result" => $result));
}

function getJSONError($errors) {
	return json_encode(array("errors" => $errors));
}


?>