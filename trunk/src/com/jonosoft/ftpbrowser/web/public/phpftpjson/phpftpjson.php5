<?


header("Content-type: text/plain");



$phpftp_host = $_REQUEST["server"];
$phpftp_port = $_REQUEST["port"];
$phpftp_user = $_REQUEST["username"];
$phpftp_passwd = $_REQUEST["password"];
$phpftp_dir = $_REQUEST["dir"];

$function = $_REQUEST["function"];

switch($function) {
    case "dir";
        phpftp_list($phpftp_user,$phpftp_passwd,$phpftp_dir);
        break;
    case "cd";
        phpftp_cd($phpftp_user,$phpftp_passwd,$phpftp_dir,$select_directory);
        break;
    case "get";
        phpftp_get($phpftp_user,$phpftp_passwd,$phpftp_dir,$select_file);
        break;
    case "put";
        phpftp_put($phpftp_user,$phpftp_passwd,$phpftp_dir,$userfile,$userfile_name);
        break;
    case "mkdir";
        phpftp_mkdir($phpftp_user,$phpftp_passwd,$phpftp_dir,$new_dir);
        break;
    case "";
        phpftp_login();
        break;
}



function phpftp_connect($phpftp_user,$phpftp_passwd) {
	global $phpftp_host;
	$ftp = ftp_connect($phpftp_host);
	if ($ftp) {
		if (ftp_login($ftp,$phpftp_user,urldecode($phpftp_passwd))) {
			return $ftp;
		}
	}
}


function phpftp_list($phpftp_user,$phpftp_passwd,$phpftp_dir) {
	global $phpftp_host;
	
	$ftp = @phpftp_connect($phpftp_user,$phpftp_passwd);
	
	if (!$ftp) {
		// FTP Login failed!
	} else {
		if (!$phpftp_dir) {
			$phpftp_dir=ftp_pwd($ftp);
		}
		if (!@ftp_chdir($ftp,$phpftp_dir)) {
			// Can't enter that directory!
			$phpftp_dir=ftp_pwd($ftp);
		}
		
		if ($phpftp_dir == "/") {
			$phpftp_dir="";
		}

		if ($contents = ftp_rawlist($ftp,"")) {
			$d_i=0;
			$f_i=0;
			$l_i=0;
			$i=0;
			while ($contents[$i]) {
				$item[] = split("[ ]+",$contents[$i],9);
				$item_type=substr($item[$i][0],0,1);
				if ($item_type == "d") {
					/* it's a directory */
					$nlist_dirs[$d_i]=$item[$i][8];
					$d_i++;
				} elseif ($item_type == "l") {
					/* it's a symlink */
					$nlist_links[$l_i]=$item[$i][8];
					$l_i++;
				} elseif ($item_type == "-") {
					/* it's a file */
					$nlist_files[$f_i]=$item[$i][8];
					$nlist_filesize[$f_i]=$item[$i][4];
					$f_i++;
				} elseif ($item_type == "+") {
					/* it's something on an anonftp server */
					$eplf=split(",",implode(" ",$item[$i]),5);
					if ($eplf[2] == "r") {
						/* it's a file */
						$nlist_files[$f_i]=trim($eplf[4]);
						$nlist_filesize[$f_i]=substr($eplf[3],1);
						$f_i++;
					} elseif ($eplf[2] == "/") {
						/* it's a directory */
						$nlist_dirs[$d_i]=trim($eplf[3]);
						$d_i++;
					}
				} /* ignore all others */
				$i++;
			}
			
			//if (count($nlist_dirs)>0) {
				//echo getJSONResult("dirs" => $nlist_dirs);
			//}
			
			//if (count($nlist_files)>0) {
				//echo getJSONResult($nlist_files);
			//}
			
			echo getJSONResult(array("dirs" => $nlist_dirs, "files" => $nlist_files));
		} else {
			//Directory empty or not readable
		}
		
		$cdup=dirname($phpftp_dir);
		if ($cdup == "") {
			$cdup="/";
		}
		
		ftp_quit($ftp);
	}
}

function getJSONResult($result) {
	return json_encode(array("result" => $result));
}


?>