1. Install FUSE, create user group and add current user to the group:

sudo apt install fuse3
sudo groupadd fuse
sudo usermod -aG fuse $USER

2. Create folder, set owner and permissions:

mkdir /mnt/cfs
sudo chown $USER:$USER /mnt/cfs

3. Allow mount as user:

Edit /etc/fuse.conf and uncomment user_allow_other

4. Remove mount, if existing: 

fusermount -u /mnt/cfs