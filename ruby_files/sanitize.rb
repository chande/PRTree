myfile = File.open("poi.txt", "r+")
outFile = File.open("sanitized.txt", "w+")

myfile.each do |line|
  lineAr = line.split(',')

  #only keep letters
  lineAr[2] = lineAr[2].downcase.gsub(/[^a-z\s]/, '').strip

  #skip any locations that may not have a name
  if lineAr[2] == ""
    next
  end

  #remove single-character words
  poi = lineAr[2].split(' ')
  for str in poi
    if str.length == 1
      str[str] = ''
    end
  end

  #ensure only one space between words
  poi = poi.join(" ")
  poi = poi.squeeze(" ").strip

  #skip locations that are empty as a result of this process
  if poi == ""
    next
  end

  #keep what's left
  outFile.write(lineAr[0] + "," + lineAr[1] + ",\"" + poi + "\"")
  outFile.write("\n")
end

myfile.close
