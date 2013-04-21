myfile = File.open("poi.txt", "r+")
outFile = File.open("noDup.txt", "w+")
count = 0
h = Hash.new
myfile.each do |line|
  lineAr = line.split(',')
  if (h[lineAr[2]] == nil)
    h[lineAr[2]] = lineAr[1]
    outFile.write(line)
  end
  count += 1
end

myfile.close
