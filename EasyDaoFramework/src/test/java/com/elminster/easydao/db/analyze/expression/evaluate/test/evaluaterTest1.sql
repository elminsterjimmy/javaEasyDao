--if a > 1 and b > 1 then
select * from A where test = 1
--elseif a < -1 or b < -1 then
select * from B where test = 2
--elseif a <> 0 then
select * from C where test = 3
--else
select * from D where test = 4
--endif