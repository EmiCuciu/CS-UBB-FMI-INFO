{
	arr[$1] += $2
}
END {
	for (u in arr){
		print u, arr[u]
	}
}

