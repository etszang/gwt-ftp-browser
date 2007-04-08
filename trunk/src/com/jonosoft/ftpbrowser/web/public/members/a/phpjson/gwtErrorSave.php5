<?


require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/includes/dbUtil.php5');
require_once($_SERVER['DOCUMENT_ROOT'].'/members/a/bo/GWTError.php5');

header("Content-type: text/plain");
session_start();

//
// REMOVE THIS LINE WHEN OUT OF DEV
//
$gwtError = new GWTError();

//$gwtError->setUserId($_SESSION["ccuserid"]);
$gwtError->setStackTrace($_REQUEST["stack_trace"]);

if (strlen(trim($gwtError->getStackTrace())) == 0) {
	echo getJSONError(array(11, "Stack trace required for GWT error"));
}
else {
	if (GWTErrorDB::save($gwtError)) {
		echo getJSONResult(array("gwtError" => "saved"));
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