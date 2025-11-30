package com.popcorntime.local.account

import com.popcorntime.repository.account.data_source.local.AccountLocalDataSource
import com.popcorntime.repository.account.data_source.local.dto.AccountLocalDto
import javax.inject.Inject

class AccountLocalDataSourceImpl @Inject constructor(
    private val accountCacheDao: AccountCacheDao
): AccountLocalDataSource {

    override suspend fun setAccount(account: AccountLocalDto) {
        accountCacheDao.insertAccount(account)
    }

    override suspend fun getAccount(): List<AccountLocalDto> {
        return accountCacheDao.getAccount()
    }

    override suspend  fun getAccountId(): Long? {
        return accountCacheDao.getAccount().getOrNull(0)?.accountId
    }

    override suspend fun removeAccount() {
        accountCacheDao.deleteAccount()
    }
}