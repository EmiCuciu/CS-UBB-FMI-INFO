{
for (i=1;i<=NF;i++)
	v[$i]++
}
END{
for (j in v)
	print " " j ": apare de " v[j] " ori "
}
